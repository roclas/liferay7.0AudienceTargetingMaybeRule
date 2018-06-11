package rule.roclas.content.targeting.rule;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.content.targeting.anonymous.users.model.AnonymousUser;
import com.liferay.content.targeting.api.model.BaseJSPRule;
import com.liferay.content.targeting.api.model.Rule;
import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.rule.categories.SampleRuleCategory;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

@Component(immediate = true, service = Rule.class)
public class RuleMaybeRule extends BaseJSPRule {

	@Activate
	@Override
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deActivate() {
		super.deActivate();
	}

	@Override
	public boolean evaluate(
			HttpServletRequest httpServletRequest, RuleInstance ruleInstance,
			AnonymousUser anonymousUser)
		throws Exception {
		// You can obtain the rule configuration from the type settings
		String typeSettings = ruleInstance.getTypeSettings();
		_logger.info("evaluating "+typeSettings);
		String option = _getOption(typeSettings);
		// Return true if the anonymous user matches this rule
		switch(option){
			case("none"):return false;
			case("all"):return true;
			case("maybe"):return Math.random()<0.5;
			default: return false;
		}

	}

	@Override
	public String getIcon() {
		return "icon-puzzle-piece";
	}

	@Override
	public String getRuleCategoryKey() {

		// Available category classes: BehaviourRuleCategory,
		// SessionAttributesRuleCategory, SocialRuleCategory and
		// UserAttributesRoleCategory

		return SampleRuleCategory.KEY;
	}

	@Override
	public String getSummary(RuleInstance ruleInstance, Locale locale) {
		String typeSettings = ruleInstance.getTypeSettings();

		String option= _getOption(typeSettings);

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		switch(option){
			case("none"):
				return LanguageUtil.get( resourceBundle, "the-user-never-matches-this-rule");
			case("all"):
				return LanguageUtil.get( resourceBundle, "the-user-always-matches-this-rule");
			case("maybe"):return LanguageUtil.get( resourceBundle, "the-may-match-this-rule");
			default: return LanguageUtil.get( resourceBundle, "the-does-not-match-this-rule");
		}

	}

	@Override
	public String processRule(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String id, Map<String, String> values) {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		String option= GetterUtil.getString(values.get("option"));

		jsonObject.put("option", option);

		return jsonObject.toString();
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=rule.maybe)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Override
	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		String option= "";

		if (!values.isEmpty()) {

			// Value from the request in case of an error

			option= GetterUtil.getString(values.get("option"));
		}
		else if (ruleInstance != null) {

			// Value from the stored configuration

			String typeSettings = ruleInstance.getTypeSettings();

			option= _getOption(typeSettings);
		}

		context.put("option", option);
	}

	private String _getOption(String typeSettings) {
		try {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(typeSettings);

			return jsonObject.getString("option");
		}
		catch (JSONException jsone) {
		}

		return "";
	}

	Logger _logger = LoggerFactory.getLogger(this.getClass().getName());


}