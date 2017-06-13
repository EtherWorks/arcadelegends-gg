package gg.al.logic.component.data;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 * Interface denoting the ability to be loaded from a {@link Template}.
 */
public interface ITemplateable {
    /**
     * @return the default {@link Template} for this class.
     */
    Template getDefaultTemplate();

    /**
     * Loads the object from a {@link Template}
     *
     * @param template the template from which to load from
     */
    void fromTemplate(Template template);
}
