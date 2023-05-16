package com.phcdevelop.preview_hook_compiler

import com.google.auto.service.AutoService
import com.phcdevelop.preview_hook_annotation.PreviewActParent
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplClazz
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplGetter
import com.phcdevelop.preview_hook_annotation.PreviewCreateAct
import com.phcdevelop.preview_hook_compiler.utils.RoundEnvironmentEx.find
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.lang.reflect.Type
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class PreviewHookProcessor : AbstractProcessor() {

    private val DEAFAULT_ACT_PARENT = "androidx.activity.ComponentActivity"
    var filer: Filer? = null

    var msg: Messager? = null

    var isCreated = false

    var elementUtils: Elements? = null
    override fun init(processingEnv: ProcessingEnvironment) {
        filer = processingEnv.filer
        msg = processingEnv.messager
        msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewHookProcessor is init!")
        elementUtils = processingEnv.elementUtils
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        if(isCreated) return true
        try {
            var packageStr = ""
            var fieldClazzName = ""
            var getterName = ""
            var actParentClazz :TypeMirror?= null
            var asyncClazz: TypeMirror?
            annotations?.find(PreviewAsyncImplGetter::class)?.let {
                    fieldClazzName = roundEnv?.find(PreviewAsyncImplClazz::class.java)?.toString()?:""
                    msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncClazz：$fieldClazzName")

                    roundEnv?.find(PreviewAsyncImplGetter::class.java)?.let {getter->
                        getterName = getter.toString()
                        asyncClazz = getter.asType()
                        msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncGetter：${getterName}")
                        msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncGetter.asType：${asyncClazz}")
                        packageStr = elementUtils?.getPackageOf(getter)?.qualifiedName?.toString()?:""
                        msg?.printMessage(Diagnostic.Kind.NOTE, "packageStr：${packageStr}")
                    }

                }

            annotations?.find(PreviewActParent::class)?.let {
                actParentClazz = roundEnv?.find(PreviewActParent::class.java)?.asType()
                msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewActParent：${actParentClazz}")
            }

            val clazzBuilder = TypeSpec.classBuilder("PreviewHook\$PreviewAct")
                .addAnnotation(
                    AnnotationSpec.builder(AutoService::class.java)
                    .addMember("value","{\$N.class}",PreviewCreateAct::class.java.simpleName)//给生成的类打上PreviewCreateAct标记
                    .build()
                )
                .addModifiers(Modifier.PUBLIC)
                .run {//是否执行了父类，默认取ComponentAct
                    if (actParentClazz == null){
                        this.superclass(Class.forName(DEAFAULT_ACT_PARENT))
                    }else{
                        this.superclass(actParentClazz)
                    }
                }
                .addSuperinterface(Class.forName("com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback"))
                .addSuperinterface(PreviewCreateAct::class.java)
                .apply {
                    //写方法
                    addMethod(
                        MethodSpec.methodBuilder("doAsync")
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(
                                Function0::class.java,
                                "doOnCreate",
                            )
                            .apply {
                                //写内容
                                msg?.printMessage(Diagnostic.Kind.NOTE, "addStatement：${fieldClazzName}.${getterName}.doAsync(doOnCreate)")
                                this.addStatement("${fieldClazzName}.${getterName}.doAsync(doOnCreate)")
                            }
                            .build()
                    )
                }

            JavaFile.builder(packageStr, clazzBuilder.build())
                .build()
                .writeTo(filer)
            isCreated = true
            return true
        } catch (e: Exception) {
            e.printStackTrace()
//            msg?.printMessage(Diagnostic.Kind.ERROR, e.message)
        }
        return false
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            PreviewActParent::class.java.canonicalName,
            PreviewAsyncImplGetter::class.java.canonicalName
        )
    }


}