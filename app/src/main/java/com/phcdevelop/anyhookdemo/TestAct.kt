package com.phcdevelop.anyhookdemo

import com.google.auto.service.AutoService
import com.phcdevelop.anyhook.preview_hook_callback.AsyncCallback
import com.phcdevelop.preview_hook_annotation.PreviewCreateAct

/**
 * @Author PHC
 * @Data 2023/5/17 14:08
 */
@AutoService(PreviewCreateAct::class)
public class TestAct : BaseAct(), AsyncCallback by
com.phcdevelop.anyhookdemo.async.AsyncInstance.instance, PreviewCreateAct