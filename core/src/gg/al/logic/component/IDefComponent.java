package gg.al.logic.component;

/**
 * Created by Thomas Neumann on 24.04.2017.<br />
 */
public interface IDefComponent {
    IComponentDef getDefaultDef();
    void fromDef(IComponentDef def);
}