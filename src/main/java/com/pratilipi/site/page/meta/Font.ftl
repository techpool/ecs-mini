<#-- Default Font -->
<#assign font_url="https://fonts.googleapis.com/css?family=Montserrat">
<#assign font_url="https://www.ptlp.co/resource-all/font/font-en.css">
<#assign font="Montserrat">

<#if language == "HINDI">
	<#assign font_url="https://fonts.googleapis.com/css?family=Noto+Sans&subset=devanagari">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-hi.css">
	<#assign font="Noto Sans">
</#if>
<#if language == "BENGALI">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosansbengali.css">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-bn.css">
    <#assign font="Noto Sans Bengali">
</#if>
<#if language == "MARATHI">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosansdevanagari.css">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-mr.css">
	<#assign font="Noto Sans Devanagari">
</#if>
<#if language == "GUJARATI">
	<#assign font_url="https://0.ptlp.co/resource-gu/font/shruti.css">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-gu.css">
	<#assign font="Shruti">
</#if>
<#if language == "TAMIL">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosanstamil.css">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-ta.css">
	<#assign font="Noto Sans Tamil">
</#if>
<#if language == "MALAYALAM">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosansmalayalam.css">
	<#assign font_url="https://www.ptlp.co/resource-all/font/font-ml.css">
	<#assign font="Noto Sans Malayalam">
</#if>
<#if language == "TELUGU">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosanstelugu.css">
    <#assign font_url="https://www.ptlp.co/resource-all/font/font-te.css">
	<#assign font="Noto Sans Telugu">
</#if>
<#if language == "KANNADA">
	<#assign font_url="http://fonts.googleapis.com/earlyaccess/notosanskannada.css">
	<#assign font_url="https://www.ptlp.co/resource-all/font/font-kn.css">
	<#assign font="Noto Sans Kannada">
</#if>
<style>@import url(${ font_url });*{font-family: Helvetica, '${ font }', sans-serif;}</style>