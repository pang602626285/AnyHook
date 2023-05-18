package com.phcdevelop.preview_hook_compiler

import com.google.auto.service.AutoService
import com.phcdevelop.base.Constants
import com.phcdevelop.preview_hook_annotation.PreviewActParent
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplClazz
import com.phcdevelop.preview_hook_annotation.PreviewAsyncImplField
import com.phcdevelop.preview_hook_annotation.PreviewCreateAct
import com.phcdevelop.preview_hook_compiler.utils.RoundEnvironmentEx.find
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class PreviewHookProcessor : AbstractProcessor() {

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
        if (isCreated) return true
        try {
            var packageStr = ""
            var fieldClazzName = ""
            var getterName = ""
            var actParentClazz: TypeName? = null
//            var asyncClazz: TypeName?
            annotations?.find(PreviewAsyncImplField::class)?.let {
                fieldClazzName = roundEnv?.find(PreviewAsyncImplClazz::class.java)?.toString() ?: ""
                msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncClazz：$fieldClazzName")

                roundEnv?.find(PreviewAsyncImplField::class.java)?.let { getter ->
                    getterName = getter.toString()
//                    asyncClazz = getter.asType().asTypeName()
                    msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncGetter：${getterName}")
//                    msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewAsyncGetter.asType：${asyncClazz}")
                    packageStr = elementUtils?.getPackageOf(getter)?.qualifiedName?.toString() ?: ""
                    msg?.printMessage(Diagnostic.Kind.NOTE, "packageStr：${packageStr}")
                }

            }
            annotations?.find(PreviewActParent::class)?.let {
                actParentClazz =
                    roundEnv?.find(PreviewActParent::class.java)?.asType()?.asTypeName()
                msg?.printMessage(Diagnostic.Kind.NOTE, "PreviewActParent：${actParentClazz}")
            }
            val clazzBuilder = TypeSpec.classBuilder(Constants.CREATE_PREVIEW_ACT_NAME)
//                .addPreviewCreate()//生成的kt用AutoService获取不到，java可以
                .run {//是否标记了父类，否则默认取ComponentAct
                    if (actParentClazz == null) {
                        this.superclass(Class.forName(DEAFAULT_ACT_PARENT).kotlin)
                    } else {
                        this.superclass(actParentClazz!!)
                    }
                }
                .addSuperinterface(
                    Class.forName("com.phcdevelop.preview_hook_api.async.AsyncCallback").kotlin,
                    delegate = CodeBlock.of("${fieldClazzName}.${getterName}")
                )

            FileSpec.builder(packageStr, Constants.CREATE_PREVIEW_ACT_NAME)
                .addType(clazzBuilder.build())
                .apply {
                    msg?.printMessage(Diagnostic.Kind.NOTE, "outputName：${this.name}")
                }
                .build()
                .writeTo(filer!!)
            saveConstants(packageStr + "." + Constants.CREATE_PREVIEW_ACT_NAME, filer)
            isCreated = true
            return true
        } catch (e: Exception) {
            e.printStackTrace()
//            msg?.printMessage(Diagnostic.Kind.ERROR, e.message)
        }
        return false
    }

    private fun saveConstants(value: String, filer: Filer?) {
        FileSpec.builder(Constants.CREATE_CONSTANTS_PACK, Constants.CREATE_CONSTANTS_NAME)
            .addType(
                TypeSpec.classBuilder(Constants.CREATE_CONSTANTS_NAME)
                    .addType(
                        TypeSpec.companionObjectBuilder()
                            .addProperty(
                                PropertySpec.builder(Constants.CREATE_FIELD_NAME, String::class)
                                    .addAnnotation(JvmField::class)
                                    .initializer("%S", value)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
            .writeTo(filer!!)
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            PreviewActParent::class.java.canonicalName,
            PreviewAsyncImplField::class.java.canonicalName
        )
    }


}

private fun TypeSpec.Builder.addPreviewCreate(): TypeSpec.Builder =
    this.addAnnotation(
        AnnotationSpec.builder(AutoService::class)
            .addMember(
                "%T::class",
                PreviewCreateAct::class
            )//给生成的类打上PreviewCreateAct标记
            .build()
    )
        .addSuperinterface(PreviewCreateAct::class)