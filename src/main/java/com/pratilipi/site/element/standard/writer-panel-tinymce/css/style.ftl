html, body { 
    margin:0; 
    padding:0; 
    height:100%; 
    min-height: 100%;
}

.editor-old{
    position: fixed !important;
    top: 0;
    left: 0;
    margin-top: 0px !important;
    width: 100%;
    padding-left: 0px;
    z-index: 1;
    border-bottom: 1px solid #ddd;

}

#header1.affix {
    position: fixed !important;
    top: 0;
    /*left: 9%;*/
    left:0;
    right: 0;
    margin-top: 0px !important;
    /*width: 82%;*/
    /*width: 100%;*/
    /*padding-left: 0px;*/
    /*padding-left: 5%;*/
    /*padding-right:5%;*/
    z-index: 1;
    border-bottom: 1px solid #ddd;
    padding-right: 0px;
    padding-left: 0px;
    margin-right: auto;
    margin-left: auto;

}

#editor.affix {
    position: fixed !important;
    top: 47px !important;
    /*left: 7%;*/
    margin-top: 0px !important;
    width: 86.75%;
    padding-left: 0px;
    left: 0;
    right: 0;
    margin-left: auto;
    margin-right: auto;
    background: white;
    border-bottom: 1px solid #ddd;
}   

#editor.affix div {
    width: auto;
    float: right;
    margin-bottom: 10px;
    margin-top: 10px;
} 

#editor.affix-top {
    top: 0px !important;
}    
#toc_button,#toc_button:active,#toc_button:focus,#toc_button:visited,#toc_button:hover {
    background: white;
   /* border-color: #ddd !important;*/
    outline: 0 none;

}
#toc_button {
    margin-top: 4px; 
}
.open #toc_button {
    border-radius: 4px 4px 0 0;
}
.horizontal-form-input {
    border: none;
    box-shadow: none !important;
    border-bottom: 1px solid black;
    border-radius: 0px;
    /*color: #a5a0a0;*/
}
.horizontal-form-input:hover, .horizontal-form-input:focus {
    box-shadow: none;
}

.horizontal-form-input:focus {
    border-color: #d0021b;
}

.left-align-content {
    text-align: left;
}
form .form-group {
    margin-bottom: 30px;
    margin-top: 15px;
}
.pratilipi-red-button {
    background: #FFFFFF;
    border: 1px solid #d0021b;
    box-shadow: 0px 1px 1px 0px rgba(0, 0, 0, 0.50);
    border-radius: 5px;
    font-size: 13px;
    color: #d0021b;
    letter-spacing: 0.3px;
    line-height: 16px;
    text-shadow: 0px 1px 2px #FFFFFF;
    display: inline-block;
    padding: 10px;
    margin-right: 10px;
    magin-top: 5px;
    outline: none;
} 
.pratilipi-red-background-button {
    background: #d0021b !important;
    border: 1px solid #d0021b !important;  /* changed*/
    box-shadow: 0px 1px 1px 0px rgba(0, 0, 0, 0.50);
    border-radius: 5px;
    font-size: 13px;
    color: #ffffff;
    letter-spacing: 0.3px;
    line-height: 16px;
    display: inline-block;
    padding: 10px;
    margin-right: 10px;
    margin-top: 5px;
    outline: none;
    font-weight: 800;
} 
.go-button {
    margin-top: 20px;
    padding-left: 20px;
    padding-right: 20px;
    font-size: 20px;
    text-shadow: none;
} 
.pratilipi-red {
    color: #d0021b;
}
.translucent {
    position: absolute;
    top: 0px;
    left: 0px;
    height: 50px;
    background: #999;
    text-align: center;
    opacity: 0.3;
    transition: opacity .13s ease-out;
    width: 100%;
    -webkit-font-smoothing: antialiased;
    padding-top: 5px;
    background: linear-gradient(black, #f9f9f9);
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    z-index: 1;
}
/* base CSS element */
div.lefttip, div.righttip {
    position: relative;
}
div.lefttip div {
    display: block;
    font-size: 16px;
    position: absolute;
    top: 12%;
    left: 50px;
    padding: 5px;
    z-index: 100;
    background: #ddd;
    color: black;
    -moz-border-radius: 5px; /* this works only in camino/firefox */
    -webkit-border-radius: 5px; /* this is just for Safari */
    text-shadow: none;
}
div.righttip div {
    display: block;
    font-size: 16px;
    position: absolute;
    top: 12%;
    right: 50px;
    padding: 5px;
    z-index: 100;
    background: #ddd;
    color: black;
    -moz-border-radius: 5px; /* this works only in camino/firefox */
    -webkit-border-radius: 5px; /* this is just for Safari */
    text-shadow: none;
}
div.lefttip div:before{
    content:'';
    display:block;
    width:0;
    height:0;
    position:absolute;

    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
    border-right:8px solid #ddd;
    left:-8px;
    top:20%;
}
div.righttip div:before{
    content:'';
    display:block;
    width:0;
    height:0;
    position:absolute;

    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
    border-left:8px solid #ddd;
    right:-8px;

    top:20%;
}
.progress-bar:before{
    content:'';
    display:block;
    width:0;
    height:0;
    position:absolute;

    border-top: 8px solid transparent;
    border-bottom: 8px solid transparent;
    border-left:8px solid #ddd;
    right:-8px;

    top:20%;
}    
.book-name {
    text-align: center;
}    
.editor-action {
    width:30px; 
    height:20px;
}
#editor a:focus, #editor a:hover {
    color: #23527c;
    text-decoration: none;
}
@media only screen and (max-width: 992px) {
    .book-name {
        text-align: left;
    }
}

@media only screen and (max-width: 1200px) {
    #header1.affix {
        /*padding-left: 4%;*/
        /*padding-right: 4%;*/
    }
}
@media only screen and (max-width: 600px) {
    .small-screen-hidden {
        display: none !important;
    }
    #editor {
        padding: 2px 0 0 0;
    }
    #chapter-content {
        margin-top: 50px !important;
    }
    #editor div {
        width: 100% !important;
        padding: 7px 0 !important;
    }
    .editor-action {
        width: 9% !important;
    }
}
@media only screen and (min-width: 768px) {
    #subtitle {
        width: 50% !important;
    }
    #editor{
        float: right;
    }

}
@media only screen and (min-width:601px) {
    .big-screen-hidden {
        display: none;
    }
    #toc_button {
      min-width:230px;
    } 
}  

/*media queries for header and editor on being scrolled*/
@media only screen and (max-width: 768px) {
    #header1.affix {
        width: -moz-calc(100% - 30px);
        width: -webkit-calc(100% - 30px);
        width: calc(100% - 30px);
    }
    #editor.affix {
        width: -moz-calc(100% * 0.95);
        width: -webkit-calc(100% * 0.95);
        width: calc( (100% - 30px) * 0.95);
    }            
}        
@media only screen and (min-width: 768px) {
    #header1.affix {
        width: 720px;
    }
    #editor.affix {
        --widthA: 720px;
        width: -moz-calc(var(--widthA)*0.95);
        width: -webkit-calc(var(--widthA)*0.95);
        width: calc(var(--widthA)*0.95);
    }            

} 
@media only screen and (min-width: 992px) {
    #header1.affix {
        width: 940px;
    }
    #editor.affix {
        --widthA: 940px;
        width: -moz-calc(var(--widthA)*0.95);
        width: -webkit-calc(var(--widthA)*0.95);
        width: calc(var(--widthA)*0.95);
    }            
}                
@media only screen and (min-width: 1200px) {
    #header1.affix {
        width: 1140px;
    }
    #editor.affix {
        --widthA: 1140px;
        width: -moz-calc(var(--widthA)*0.95);
        width: -webkit-calc(var(--widthA)*0.95);
        width: calc(var(--widthA)*0.95);
    }            
}
  
.popover-content {
    min-width: 100px;
}                      
.right23 {
    right: -23% !important
}     
.left25 {
    left: -25% !important;
}

[contenteditable=true]:empty:before{
    content: attr(placeholder);
    display: block; /* For Firefox */
    color: #a5a0a0;
}
#chapter-content a {

}

blockquote {
    text-align: left !important;
}
.popover-content {
    word-wrap: break-word;
    /*max-width: none;*/
    /*max-width: 100%;*/
}
.writer-image {
    display:block;
    margin-left:auto;
    margin-right:auto;
}
.current-chapter {
    color: #d0021b !important;
    background-color: whitesmoke !important;
}
.small-spinner {
    position: relative;
}

.small-spinner:before, .small-spinner:after {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -10px;
    margin-top: -10px;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: #333;
    opacity: 0.6;
    content: '';
    
    -webkit-animation: sk-bounce 2.0s infinite ease-in-out;
    animation: sk-bounce 2.0s infinite ease-in-out;
}

.small-spinner:after {
    -webkit-animation-delay: -1.0s;
    animation-delay: -1.0s;
}

.spinner {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(255,255,255,0.5);
}

.spinner:before, .spinner:after {
    position: absolute;
    top: 50%;
    left: 50%;
    margin-left: -35px;
    margin-top: -35px;
    width: 70px;
    height: 70px;
    border-radius: 50%;
    background-color: #333;
    opacity: 0.6;
    content: '';
    
    -webkit-animation: sk-bounce 2.0s infinite ease-in-out;
    animation: sk-bounce 2.0s infinite ease-in-out;
}

.spinner:after {
    -webkit-animation-delay: -1.0s;
    animation-delay: -1.0s;
}

@-webkit-keyframes sk-bounce {
    0%, 100% { -webkit-transform: scale(0.0) }
    50% { -webkit-transform: scale(1.0) }
}

@keyframes sk-bounce {
  0%, 100% { 
    transform: scale(0.0);
    -webkit-transform: scale(0.0);
  } 50% { 
    transform: scale(1.0);
    -webkit-transform: scale(1.0);
  }
}

.blur-image {
    opacity: 0.6;
}

.mobile-blur-image {
    width: 240px;
}

.opaque-image{
    opacity: 1;
}
  
.title-change-input {
    width: 90%;
    margin-left: auto !important;
    margin-right: auto !important;
} 
.pratilipi-red-background-modal-button {
    border-radius: 4px;
    letter-spacing: 0.3px;
    display: inline-block;
    background: #d0021b !important;
    border: none;
    box-shadow: 0px 1px 1px 0px rgba(0, 0, 0, 0.50);
    background-color: white;
    outline: none;
    color: white;
    padding: 6px 12px;
    font-size: 14px;
}
#toast-container>.toast-success {
    background-image: none !important;  
} 
#toast-container.toast-top-center>div {
    margin-left: auto;
    margin-right: auto;
    max-width: 650px;
    width: auto;
} 
.toast {
    margin-top: 4px !important;
    background-color: grey;
    font-size: 18px;
    display: flex;
    justify-content: center;
    align-items: center; 
}
.popover-content {
    text-align: center;
}

.show-cursor {
    cursor: pointer;
}
.modal-form {
    width: 90%;
    margin-left: auto;
    margin-right: auto;
}
.grey-label {
    color: #a5a0a0;
    font-weight: 500;
}

.grey-color {
    color:#a5a0a0;
 }
 
.dimension-20 {
    width: 20px;
    height: 20px;
}
.image-container {
    margin-left: 15px;
    position:relative;
}
.cover-image {
    height: 150px;
    width: 115px;
}    
 .camera-icon {
    position:absolute;
    top:50%;
    left:35%;
 }
 .grey-background {
    background-color: #eee;
}

.btn-default:focus {
    background-color: white;
    outline: none;
}

.inputapi-transliterate-indic-suggestion-menu  {
    z-index: 1051 !important;
}

.white-space-normal {
    white-space: normal !important;
}
#summary:focus{
    border-color: #d0021b;
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(208,2,27,0.5);
}

.inputapi-transliterate-indic-suggestion-menuitem-highlight {
    background-color: #d0021b !important;
    border-color: #d0021b !important;
    font-weight: 800;
}

#chapter-content p {
    margin: 0 0 7px;
}
.sprites-icon {
    background-image: url(resources/icons-writer.svg);
    background-size: 24px 240px;
    display: inline-block;
    width: 24px;
    height: 24px;
    vertical-align: middle;
}
.sprites-icon-size-16 {
    background-image: url(resources/icons-writer.svg);
    background-size: 16px 160px;
    display: inline-block;
    width: 16px;
    height: 16px;
    vertical-align: middle;
}
.sprites-icon-size-36 {
    background-image: url(resources/icons-writer.svg);
    background-size: 36px 360px;
    display: inline-block;
    width: 36px;
    height: 36px;
    vertical-align: middle;
}
.left-arrow-icon {
    background-position:0 0;
}
.cross-icon {
    background-position:0 -24px;
}
.edit-icon {
    margin-right: 10px;
    background-position:0 -48px;
}
.delete-icon {
    background-position: 0 -72px;
}
.angle-down-icon {
    background-position: 0 -96px;
}
.red-circle-icon-16 {
    background-position: 0 -80px;
}
.list-icon {
    background-position: 0 -144px;
}
.chevron-left-icon-36 { 
    background-position: 0 108px;
}

.chevron-right-icon-36 {
    background-position: 0 72px;
}
.camera-sprite-icon {
    background-position: 0 24px;
}

@media only screen and (max-device-width: 549px)  {

    #mce-modal-block {
    }

    .mce-window {
        width: auto !important;
        top: 0 !important;
        left: 0 !important;
        right: 0 !important;
        bottom: 0 !important;
        background: none !important;
  }

    .mce-window-head {
        background: #fff !important;
    }

    .mce-window-body {
        background: #fff !important;
    }

    .mce-foot {
    }

    .mce-foot > .mce-container-body {
        padding: 10px !important;
    }

    .mce-foot button {
    }

    .mce-panel {
        max-width: 100% !important;
    }

    .mce-container {
        max-width: 100% !important;
        height: auto !important;
    }

    .mce-container-body {
        max-width: 100% !important;
        height: auto !important;
    }

    .mce-form {
        padding: 10px !important;
    }

    .mce-tabs {
        max-width: 100% !important;
    }

    .mce-tabs .mce-tab, .mce-tabs .mce-tab.mce-active {
    }

    .mce-formitem {
        margin: 10px 0 !important;
    }

    .mce-btn > button {
    }

    .mce-abs-layout-item {
        position: static !important;
        width: auto !important;
    }

    .mce-abs-layout-item.mce-label {
        display: block !important;
    }

    .mce-abs-layout-item.mce-textbox {
        -webkit-box-sizing: border-box !important;
        -moz-box-sizing: border-box !important;
        box-sizing: border-box !important;
        display: block !important;
        width: 100% !important;
    }

    .mce-abs-layout-item.mce-combobox {
        display: flex !important;
    }

    .mce-abs-layout-item.mce-combobox > .mce-textbox {
        -ms-flex: 1 1 auto;
        -webkit-flex: 1 1 auto;
        flex: 1 1 auto;
        height: 29px !important;
    }
}
body {
    font-family: Helvetica, 'Noto Sans', sans-serif;
}
#chapter-content {
    margin-top: 35px;
    margin-bottom: 30px;
    border: whitesmoke 1px solid;
    padding: 10px;
    font-size: 20px;
    min-height: 400px;
}

.word-suggester {
    position: absolute;
    padding: 10px;
    border: whitesmoke 1px solid;
    display: inline-block;
    min-width: 100px;
    display: none;
    background: white;
    z-index: 65538;
    font-size: 16px;
  }

.word-input {
    border: none;
    display: inline-block;
    border-right: 2px grey solid;
}

.suggestions {

}

.suggestion {
    cursor: pointer;
}
.highlight-suggestion {
    background: lightgrey;
    font-weight: 700;
}
blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
}  
ul,ol{
    list-style-position: inside;
}
.mce-edit-focus {
    outline: none !important;
}   
#chapter-content img {
    display: block;
    margin: 8px auto;
    max-width: 100%;
    height: auto; 
}   
div.mce-tinymce-inline {
    display: block !important;
    z-index: 999 !important;
}
.mce-floatpanel.mce-fixed {
    top: 53px !important;
}

button[disabled], html input[disabled] {
    cursor: not-allowed;
    filter: alpha(opacity=65);
    -webkit-box-shadow: none;
    box-shadow: none;
    opacity: .75;
}
#book_name_toc {
    max-width: 250px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    display: inline-block;
    margin-top: 0;
    margin-bottom: 0;
    line-height: 24px;
}

.category-save-button-disabled {
	background-color: rgba(0,0,0,0.12) !important;
	color: rgba(0,0,0,0.26) !important;
}
.pratilipi-tag-container {
 	display: flex;
	flex-wrap: wrap;
}
.pratilipi-tag-element {
	padding-top: 3px;
	padding-bottom: 3px;
	padding-left: 10px;
	padding-right: 10px;
	margin: 5px;
	text-align: center;
	border: solid 1px black;
	border-radius: 20px;
	cursor:pointer;
}
.pratilipi-tag-checked {
	background: #D0021B;
	border: solid 1px #D0021B;
	color: #FFF;	
} 
.subtitle-span {
	font-size: 80%;
	color: #4A4A4A;
}
.category-message-span {
    color: #D3D3D3;
    padding: 6px 12px;
    font-size: 14px;
    display: inline-block;
    visibility: hidden;
}
.category-failed-message {
	color: #D0021B !important;
}