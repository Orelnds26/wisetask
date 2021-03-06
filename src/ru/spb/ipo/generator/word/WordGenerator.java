package ru.spb.ipo.generator.word;

import ru.spb.ipo.generator.base.BaseGenerator;
import ru.spb.ipo.generator.base.ComplexElement;
import ru.spb.ipo.generator.base.ListElement;
import ru.spb.ipo.generator.base.ui.BaseGeneratorUI;
import ru.spb.ipo.generator.base.ui.ConstraintPanel;
import ru.spb.ipo.generator.cards.TypeModell.Generator;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WordGenerator extends BaseGeneratorUI {

    public WordGenerator() {
        initialize();
    }

    protected Dimension getGeneratorSize() {
        return new Dimension(880, 450);
    }

    public static void main(String[] args) {
        new WordGenerator().setVisible(true);
    }

    protected ConstraintPanel getSetPanel() {
        if (setPanel == null) {
            setPanel = new WordSet(this);
        }
        return setPanel;
    }

    protected ConstraintPanel getFunctionPanel() {
        if (functionPanel == null) {
            functionPanel = new WordFPanel(this);
        }
        return functionPanel;
    }

    public static ListElement[] getTokenList() {
        ListElement[] list = new ListElement[30];
        HashSet set = new HashSet();
        set.add('а');
        set.add('е');
        set.add('ё');
        set.add('и');
        set.add('о');
        set.add('у');
        set.add('э');
        set.add('ю');
        set.add('я');
        int p = 0;
        for (int i = 0; i < 32; i++) {
            char c = (char) ('а' + i);
            if ('ъ' == c || 'ь' == c) {
                continue;
            }
            list[p++] = new ListElement("" + c, new CharGenerator(c, set.contains(c) ? 1 : 0));
        }
        return list;
    }

    protected void fillParameters(Map source, Map func, Map task) {
        source.put("nabor", ((WordSet) getSetPanel()).getNabor().getSelectedItem());
        source.put("half-length", "" + Integer.valueOf((String) ((WordSet) getSetPanel()).getNabor().getSelectedItem()) / 2);
        source.put("setType-template", ((WordFPanel) getFunctionPanel()).getIsSingle().isSelected());

        List functs = ((WordSet) getSetPanel()).getAlphabit();
        StringBuffer setElements = new StringBuffer();
        for (int i = 0; i < functs.size(); i++) {
            ListElement pe = (ListElement) functs.get(i);
            setElements.append(pe.generateXml());
        }
        source.put("set-elements", setElements.toString());
        task.put("alphabit", ((WordSet) getSetPanel()).getTokenList().getText());
    }

    public BaseGenerator createGenerator(Map source, Map func, Map task) {
        return new WordXmlGenerator(source, func, task);
    }

    static class CharGenerator extends Generator implements ComplexElement {

        private char ch;

        private int type;

        public CharGenerator(char ch, int type) {
            super(null);
            this.ch = ch;
            this.type = type;
        }

        public String generateXml() {
            StringBuffer sb = new StringBuffer();
            sb.append("<constElement>\n");
            sb.append("    <constElement>" + (ch - 'а') + "</constElement>");
            sb.append("    <constElement>" + type + "</constElement>");
            sb.append("</constElement>\n");
            return sb.toString();
        }

        public String toDescription() {
            return toString();
        }

        public String toString() {
            return "" + ch;
        }
    }

    class WordElement extends Generator implements ComplexElement {
        public WordElement(String syr) {
            super(null);
        }

        public String generateXml() {
            return super.generateXml();
        }

        public String toDescription() {
            return null;
        }
    }

    public String getHelpString() {
        //return "Конструктор задач со словами";
        return "Редактор \"Слова над конечным алфавитом\"";
    }

    private static BaseGeneratorUI instance = null;

    public static BaseGeneratorUI getInstance() {
        if (instance == null) {
            return instance = new WordGenerator();
        }
        return instance;
    }

    protected void clear() {
        super.clear();
        ((WordSet) getSetPanel()).getJButton().doClick();
    }
}
