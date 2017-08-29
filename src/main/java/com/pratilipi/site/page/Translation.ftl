<html>

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pratilipi Language Translations</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.grey-red.min.css" />
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/dialog-polyfill/0.4.6/dialog-polyfill.js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/dialog-polyfill/0.4.6/dialog-polyfill.css" />
    <link href="https://fonts.googleapis.com/css?family=Noto+Sans" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://0.ptlp.co/resource-all/rangy_core.rangy_selectionsaverestore.min.js"></script>
    <script src="/resources/js/tinymce-writer/suggester.js"></script>
    <script src="/resources/js/tinymce-writer/app.js"></script>
    <style>
    html {
        font-family: 'Noto Sans', sans-serif;
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
    
    .demo-list-control {
        width: 300px;
    }
    
    .demo-list-radio {
        display: inline;
    }
    
    .mdl-dialog {
        width: 100%;
        height: 90%;
    }
    
    .demo-card-wide.mdl-card {
        width: 512px;
    }
    
    .demo-card-wide > .mdl-card__title {
        color: #fff;
        height: 256px;
        box-shadow: inset 0 0 0 1000px rgba(0, 0, 0, 0.2);
    }
    
    .demo-card-wide > .mdl-card__menu {
        color: #fff;
    }
    
    .email {
        background: url('http://i.imgur.com/Igzq3y0.jpg') center / cover;
    }
    
    .notif {
        background: url('http://i.imgur.com/ivt2BdL.jpg') center / cover;
    }
    
    .mdl-grid {
        padding-top: 7%;
        margin-left: 5%;
        width: 100%;
    }
    
    .pratilipi-logo {
        width: 70%;
    }
    
    .full-width {
        width: 80%;
    }
    
    .mdl-textfield {
        width: 100%!important;
        max-width: 100%!important;
    }
    
    .mdl-data-table {
        table-layout: fixed;
        width: 100%;
    }
    
    .action-form td,
    th {
        width: auto;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
    }
    
    td {
        border-top: 0px!important;
        border-bottom: 0px!important;
    }

    th {
        text-align: center!important;
    }
    /* unrelated to responsive table css */
    
    .action-form {
        margin-top: 7%;
    }
    
    .material-icons {
        vertical-align: -25%;
    }
    header{
        position: fixed!important;
    }
    </style>
</head>

<body>
    <div class="mdl-layout mdl-js-layout mdl-layout--no-desktop-drawer-button">
        <header class="mdl-layout__header mdl-layout__header--scroll myHeader">
        </header>
        <main>
            <div class="mdl-grid option-selector">
                <div class="mdl-cell mdl-cell--6-col">
                    <div class="demo-card-wide mdl-card mdl-shadow--2dp">
                        <div class="mdl-card__title  email">
                            <h2 class="mdl-card__title-text">Email Translations</h2>
                        </div>
                        <div class="mdl-card__supporting-text">
                            All language translations related to content sent in email communications - both transactional and promotional in nature.
                        </div>
                        <div class="mdl-card__actions mdl-card--border">
                            <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent continue-over" value="EMAIL">
                                Continue
                            </button>
                        </div>
                    </div>
                </div>
                <div class="mdl-cell mdl-cell--6-col">
                    <div class="demo-card-wide mdl-card mdl-shadow--2dp">
                        <div class="mdl-card__title  notif">
                            <h2 class="mdl-card__title-text">Notification Translations</h2>
                        </div>
                        <div class="mdl-card__supporting-text">
                            All language translations related to content shown as notifications on web and mobile products.
                        </div>
                        <div class="mdl-card__actions mdl-card--border">
                            <button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent continue-over" value="NOTIFICATION">
                                Continue
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-table">
                <table class="mdl-data-table mdl-js-data-table mdl-data-table--selectable mdl-shadow--2dp action-form full-width">
                </table>
            </div>
    </div>
    <div class="word-suggester">
      <div class="word-input"></div>
      <div class="suggestions">
        <div class="suggestion"></div>
      </div>
    </div>
    </main>
    </div>
</body>
<script>
var LANGS = {
  'hindi':'hi',
  'bengali':'bn',
  'gujarati':'gu',
  'kannada':'kn',
  'malayalam':'ml',
  'marathi':'mr',
  'tamil':'ta',
  'telugu':'te',
  }
var basicHeader = '<div class="mdl-layout__header-row"><span class="mdl-layout-title"><img src="http://public.pratilipi.com/pratilipi-logo/png/Logo-2C-RGB-80px.png" class="pratilipi-logo"/></span>';
var submitHeader = basicHeader + '<div class="mdl-layout-spacer"></div><nav class="mdl-navigation"><button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent back-to-options" value="BACK">Back</button><button class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent submit-form" value="SUBMIT">Submit</button></nav>'
$('.myHeader').html(basicHeader + '</div>');
// var language = window.location.hostname.split(".")[0];
var language = 'hindi';
$('.continue-over').click(function() {
    // $('.option-selector').html('<img src="http://www.downgraf.com/wp-content/uploads/2014/09/01-progress.gif"/>');
    var group = $(this).val();
    $.ajax({
        type: 'GET',
        // url: '/api/i18n?language=' + language + '&group=' + group,
        url: '/api?requests={%22req1%22:%22/i18n?group=' + group + '%26language=' + language.toUpperCase() + '%22,%22req2%22:%22/i18n?group=' + group + '%26language=ENGLISH%22}',
        success: function(data) {
            var englishData = data["req2"]["response"]["keyValues"];
            var langData = data["req1"]["response"]["keyValues"];
            var formInit = "<form class='myForm'>";
            var formEnd = "</form>";
            var tHead = '<thead><tr><th class="mdl-data-table__cell--non-numeric">English</th><th>' + language + '</th></tr></thead>';
            var tBodyInit = "<tbody>";
            var tBodyEnd = "</tbody>";
            var textFieldInit = '<div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">';
            var textFieldEnd = '</div>';
            var inputField = '<input class="mdl-textfield__input text-input my-input" type="text" id="sample';
            var labelField = '<label class="mdl-textfield__label" for="sample';
            var i = 0;
            var inLoop = "";
            for (var e in englishData){
                for (var l in langData){
                    if (e==l){
                        inLoop = inLoop + '<tr>' + '<td class=mdl-data-table__cell--non-numeric">' + englishData[e] + '</td>' + '<td>' + textFieldInit + inputField + i + '" name="' + e +'" value="' + langData[l] + '"/>'+ labelField + i + '">' + langData[l] + '</label>' + textFieldEnd + '</td>' + '</tr>';
                        i = i + 1; 
                    }
                }
            }
            var out = tHead + tBodyInit + formInit + inLoop + formEnd + tBodyEnd;
            $('.action-form').html(out);
            $('.option-selector').hide();
            componentHandler.upgradeDom();
            $('.action-form').show();
            var $inT = $('.my-input')
            var inputTransObj = {};
            $(".my-input").each(function() {
                inputTransObj[$(this).attr("name")] = new transliterationApp($(this), LANGS[language]);
                inputTransObj[$(this).attr("name")].init();
            });
            $('.myHeader').html(submitHeader + '</div>');
        }
    })
  $(".myHeader").on("click",'.back-to-options',function(event){
      var answer = confirm("Do you want to continue?");
      if (answer){
          $('.myHeader').html(basicHeader+'</div>');
          $('.action-form').hide();
          $('.option-selector').show();     
      }
  });
  $(".myHeader").on("click",'.submit-form',function(event){
      var answer = confirm("Are you sure?");
      if (answer){
          var keyValues = {};
          $(".my-input").each(function() {
              keyValues[$(this).attr("name")] = $(this).val();
          });
          var settings = {
            "async": true,
            "crossDomain": true,
            "url": "/api/i18n",
            "method": "POST",
            "headers": {
              "content-type": "application/x-www-form-urlencoded",
              "cache-control": "no-cache",
              },
            "data": {
              "language": language.toUpperCase(),
              "keyValues": JSON.stringify(keyValues),
              "group": group,
            }
          }
          console.log(settings['url']);
          console.log(settings['data']);
          $.ajax(settings).done(function (response) {
            console.log(response);
          });        
      }
  });
});
</script>
</html>
