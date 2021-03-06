<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:redirect="http://xml.apache.org/xalan/redirect"
                extension-element-prefixes="redirect"
                xmlns:xalan="http://xml.apache.org/xslt">
                <!-- keeps xml looking good :)-->
<xsl:output method="xml" indent="yes" omit-xml-declaration="no" xalan:indent-amount="2"
doctype-public="databaseQueryTemplate.dtd"
  doctype-system="databaseQueryTemplate.dtd"
/>

<!-- removes white spaces -->
   <xsl:strip-space elements="*"/>
   <xsl:template match="node()|@*">
    <xsl:copy>
        <xsl:apply-templates select="node()|@*" />
    </xsl:copy>
  </xsl:template>
  
<!-- removes tags RoleIndex and RoleScorecard -->
<xsl:template match="RoleIndex|RoleScorecard"/>
</xsl:stylesheet>
