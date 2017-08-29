<#if stage!="prod">
User-agent: *
Disallow: /
<#else>
User-agent: *
Disallow: /admin/
</#if>