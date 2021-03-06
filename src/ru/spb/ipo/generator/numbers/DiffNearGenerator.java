package ru.spb.ipo.generator.numbers;

import ru.spb.ipo.generator.base.ComplexElement;
import ru.spb.ipo.generator.cards.TypeModell.Generator;

public class DiffNearGenerator extends Generator implements ComplexElement {
    private String desc;
    private int type;

    public DiffNearGenerator(String desc) {
        super(null);
        this.desc = desc;
    }

    public String toDescription() {
        return desc;
    }

    public String toString() {
        return toDescription();
    }

    public String generateXml() {
        StringBuffer sb = new StringBuffer();
        sb.append("<function type=\"And\">\n");
        sb.append("<constElement>1</constElement>\n");

        sb.append("<for name=\"i\" first=\"1\" last=\"${length}-1\" inc=\"1\">\n");
        sb.append("<function type=\"Not\">\n");
        sb.append("<function type=\"Equals\">\n");

        sb.append("<function type=\"Projection\" axis=\"${i}\">\n");
        sb.append("		<current-set-element/>\n");
        sb.append("</function>\n");

        sb.append("<function type=\"Projection\" axis=\"${i}+1\">\n");
        sb.append("		<current-set-element/>\n");
        sb.append("</function>\n");


        sb.append("</function>\n");
        sb.append("</function>\n");
        sb.append("</for>");
        sb.append("</function>\n");

        return sb.toString();
    }

}
