package ru.spb.ipo.generator.word;

import ru.spb.ipo.generator.base.ui.BaseGeneratorUI;

import javax.swing.*;
import java.awt.*;

public class IndexFPanel extends WordFPanel {

    private JPanel restrict;
    private JTextField word;

    public IndexFPanel(BaseGeneratorUI gen) {
        super(gen);
        add(getRestrict(), 0);
    }

    private JPanel getRestrict() {
        if (restrict == null) {
            restrict = new JPanel(new FlowLayout(FlowLayout.LEFT));
            restrict.add(new JLabel("Слово для индексации  "));
            restrict.add(getWord());
        }
        return restrict;
    }

    protected JTextField getWord() {
        if (word == null) {
            word = new JTextField(18);
        }
        return word;
    }


}
