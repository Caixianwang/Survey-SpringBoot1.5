/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.uiColor = '#FFFFFF';
	//config.extraPlugins ='uploadimage,image2',

	// Upload images to a CKFinder connector (note that the response type is set to JSON).
	config.uploadUrl= '/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files&responseType=json',

	// Configure your file manager integration. This example uses CKFinder 3 for PHP.
	//config.filebrowserBrowseUrl= '/ckfinder/ckfinder.html',
	//config.filebrowserImageBrowseUrl= '/#/FileBrowse',
	//config.filebrowserUploadUrl= 'api/pub/uploadFile?type=Files',
	//config.filebrowserImageUploadUrl= 'api/pub/uploadFile',
	config.filebrowserImageUploadUrl= 'file/ckUploadFile',

	// The following options are not necessary and are used here for presentation purposes only.
	// They configure the Styles drop-down list and widgets to use classes.

	//config.stylesSet= [
	//{ name: 'Narrow image', type: 'widget', widget: 'image', attributes: { 'class': 'image-narrow' } },
	//{ name: 'Wide image', type: 'widget', widget: 'image', attributes: { 'class': 'image-wide' } }
	//],

	// Load the default contents.css file plus customizations for this sample.
	//config.contentsCss= [ CKEDITOR.basePath + 'contents.css', 'http://sdk.ckeditor.com/samples/assets/css/widgetstyles.css' ],

	// Configure the Enhanced Image plugin to use classes instead of styles and to disable the
	// resizer (because image size is controlled by widget styles or the image takes maximum
	// 100% of the editor width).
	//config.image2_alignClasses= [ 'image-align-left', 'image-align-center', 'image-align-right' ],
	config.image2_disableResizer= false
	config.height = 160;
	//工具栏是否可以被收缩
	config.toolbarCanCollapse = true;
	//工具栏的位置
	//config.toolbarLocation = 'top';//可选：top,bottom
	//工具栏默认是否展开
	//config.toolbarStartupExpanded = false;
	config.toolbar_Full1 = [
		['Source','-','Save','NewPage','Preview','-','Templates'],
		['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
		['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
		['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],
		'/',
		['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
		['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		['Link','Unlink','Anchor'],
		['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
		'/',
		['Styles','Format','Font','FontSize'],
		['TextColor','BGColor']
	];	 
    config.toolbar_IntroducEnd =[
                         		['Source','Bold','Italic','Underline','Strike','Subscript','Superscript','Font','FontSize','TextColor','BGColor','Link','Unlink','Image'],
                         		//加粗     斜体，     下划线      穿过线      下标字        上标字
//                         		Paste = 粘贴
//                         		PasteText = 粘贴为无格式文本
//                         		PasteFromWord = 从 MS WORD 粘贴
                         		//[],
                         		//'Paste','PasteText','RemoveFormat'
                         		//数字列表          实体列表            减小缩进    增大缩进
                         		//['NumberedList','BulletedList','-','Outdent','Indent'],
                         		//左对齐             居中对齐          右对齐          两端对齐
                         		//['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                         		//超链接  取消超链接 锚点
                         		//['Link','Unlink','Anchor'],
                         		//图片    flash    表格       水平线            表情       特殊字符        分页符
                         		//['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
                         		//'/',
                         		//样式       格式      字体   
                         		//['Styles','Format','Font'],
                         		// 字体大小	文本颜色     背景颜色
                         		//['FontSize','TextColor','BGColor']
                         		//全屏           显示区块
                         		//['Maximize', 'ShowBlocks','-']
                         	];
    config.toolbar_QuesEdit =[
                         		['Source','Bold','Italic','Underline','Strike','Subscript','Superscript','Font','FontSize','TextColor','BGColor','Image'],
                         		//加粗     斜体，     下划线      穿过线      下标字        上标字
//                         		Paste = 粘贴
//                         		PasteText = 粘贴为无格式文本
//                         		PasteFromWord = 从 MS WORD 粘贴
                         		//[],
                         		//'Paste','PasteText','RemoveFormat'
                         		//数字列表          实体列表            减小缩进    增大缩进
                         		//['NumberedList','BulletedList','-','Outdent','Indent'],
                         		//左对齐             居中对齐          右对齐          两端对齐
                         		//['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                         		//超链接  取消超链接 锚点
                         		//['Link','Unlink','Anchor'],
                         		//图片    flash    表格       水平线            表情       特殊字符        分页符
                         		//['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
                         		//'/',
                         		//样式       格式      字体   
                         		//['Styles','Format','Font'],
                         		// 字体大小	文本颜色     背景颜色
                         		//['FontSize','TextColor','BGColor']
                         		//全屏           显示区块
                         		//['Maximize', 'ShowBlocks','-']
                         	];

    config.toolbar_Basic1 =[
		['Source','-','NewPage','Preview'],
		//加粗     斜体，     下划线      穿过线      下标字        上标字
		['Bold','Italic','Underline','Strike','Subscript','Superscript'],
		//数字列表          实体列表            减小缩进    增大缩进
		//['NumberedList','BulletedList','-','Outdent','Indent'],
		//左对齐             居中对齐          右对齐          两端对齐
		['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		//超链接  取消超链接 锚点
		['Image','Link','Unlink','Anchor'],
		//图片    flash    表格       水平线            表情       特殊字符        分页符
		//['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
		'/',
		//样式       格式      字体   
		['Styles','Format','Font'],
		// 字体大小	文本颜色     背景颜色
		['FontSize','TextColor','BGColor'],
		//全屏           显示区块
		['Maximize', 'ShowBlocks','-']
	];
	config.toolbar_Full =[
		['Source','NewPage','Preview'],
		['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
		['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
		//加粗     斜体，     下划线      穿过线      下标字        上标字
		['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		//数字列表          实体列表            减小缩进    增大缩进
		['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
		//超链接  取消超链接 锚点
		['Link','Unlink','Anchor'],
		'/',
		//左对齐             居中对齐          右对齐          两端对齐
		['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
		//图片    表格       水平线            表情       特殊字符        分页符
		['Image','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
		//样式       格式      字体  字体大小
		['Styles','Format','Font'],
		// 字体大小	文本颜色     背景颜色
		['FontSize','TextColor','BGColor'],
		//全屏           显示区块
		['Maximize', 'ShowBlocks','-']

	];
	config.toolbar_MaxBasic =[

		//加粗     斜体，     下划线 
		['Bold','Italic','Underline','Image','Table','TextColor','BGColor','Font','FontSize'],
		['JustifyLeft','JustifyCenter','JustifyRight'],	
		['HorizontalRule','Smiley','SpecialChar','Maximize'],
		

	];
	config.toolbar_Basic =[

		//加粗     斜体，     下划线 
		['Bold','Italic','Underline','Image','Table','TextColor','BGColor','Font','FontSize'],
		['JustifyLeft','JustifyCenter','JustifyRight'],	
		['HorizontalRule','Smiley','SpecialChar','Maximize'],

	];
	config.toolbar_MinBasic =[

		//加粗     斜体，     下划线 
		['Bold','Italic','Underline','Image','TextColor','BGColor','Font','FontSize'],
		['JustifyLeft','JustifyCenter','JustifyRight'],	
		['Smiley','SpecialChar'],

	];
    
    //config.toolbar='Basic';
    config.forcePasteAsPlainText = false;
    //页面载入时，编辑框是否立即获得焦点
    //config.startupFocus = true;
    //移除元素路径
    //config.removePlugins = 'elementspath';
	config.removePlugins = 'elementspath,resize';
	config.extraPlugins = 'tableresize,divarea';
    //移除状态栏
    //config.resize_enabled = false;
    //去除CKEditor自动添加的<p></p>标签
    //config.enterMode = CKEDITOR.ENTER_BR;//屏蔽换行符<br>
    //config.shiftEnterMode = CKEDITOR.ENTER_P;//屏蔽段落<p>
    config.font_names='宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;'+ config.font_names;
    //config.removeDialogTabs = 'image:advanced;image:link';
    //config.removeDialogTabs = 'image:info';
    //config.removeDialogTabs = 'image:link';
    //config.removeDialogTabs = 'image:Upload';
	//config.removePlugins = 'image2';
};
