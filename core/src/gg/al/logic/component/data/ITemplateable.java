package gg.al.logic.component.data;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 */
public interface ITemplateable {
    Template getTemplate();
    void fromTemplate(Template template);
}
