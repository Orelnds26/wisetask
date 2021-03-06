package ru.spb.ipo.generator.base.ui;

import ru.spb.ipo.generator.base.ComplexElement;

import javax.swing.*;
import java.util.Map;

public class ConstraintPanel extends JPanel {

    private BaseGeneratorUI gen;

    public ConstraintPanel(BaseGeneratorUI gen) {
        this.gen = gen;
    }

    //add condition to condition list
    public void addCondition(ComplexElement element) {
        ((DefaultListModel) gen.getFunctionList().getModel()).add(gen.getFunctionList().getModel().getSize(), element);
    }

    public ComplexElement[] getConditions() {
        return gen.getConditions();
    }

    //set current ui parameters to maps for task generation process
    public void fillMaps(Map source, Map func, Map task) {

    }

    public BaseGeneratorUI getGenerator() {
        return gen;
    }
}
