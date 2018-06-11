<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>

<%@ page import="java.util.Map" %>

<liferay-theme:defineObjects />

<portlet:defineObjects />

<%
Map<String, Object> context = (Map<String, Object>)request.getAttribute("context");
%>

<aui:select label="RuleDefaultRule" name="option" value='<%= GetterUtil.getBoolean(context.get("option")) %>'>
	<aui:option label="the-user-always-matches-this-rule" value="all" />
	<aui:option label="the-user-never-matches-this-rule" value="none" />
	<aui:option label="the-user-may-match-this-rule" value="maybe" />
</aui:select>