package ru.spb.ipo.generator.word;

import ru.spb.ipo.generator.base.BaseGenerator;

import java.util.HashMap;
import java.util.Map;

public class WordXmlGenerator extends BaseGenerator {

    public WordXmlGenerator(Map sourceParams, Map funcParams, Map taskParams) {
        super(sourceParams, funcParams, taskParams);
    }

    public String getParams() {
        String genParam = "<description-params>\n" +
                "	<param name=\"length\">\n" +
                " 		<value>${nabor}</value>\n" +
                " 	</param>\n" +
                "	<param name=\"lengthMod2\">\n" +
                " 		<value>${half-length}</value>\n" +
                " 	</param>\n" +
                "</description-params>";
        return genParam;
    }

    public String getSourceTemplate() {
        Boolean b = (Boolean) sourceParams.get("setType-template");
        int nabor = Integer.valueOf((String) sourceParams.get("nabor")).intValue();
        String source = "";
        if (b.booleanValue()) {
            source = " <sourceSet> \n <set type=\"DecartSet\" length=\"${length}\"> \n";
            for (int i = 0; i < nabor; i++) {
                source += "	<set type=\"EnumerationSet\">\n" +
                        "${set-elements}" +
                        " 		</set>\n";
            }
            source += "	</set>\n" +
                    "</sourceSet>";
        } else {
            source = " <sourceSet> \n <set type=\"LayoutSet\" length=\"${length}\"> \n" +
                    "	<set type=\"EnumerationSet\">\n" +
                    "${set-elements}" +
                    " 		</set>\n" +
                    "	</set>\n" +
                    "</sourceSet>";
        }
        source = replace(source, getBaseSourceParameters());
        return source;
    }

    protected Map getBaseSourceParameters() {
        HashMap m = new HashMap();
        //m.put("setType", "LayoutSet");
        return m;
    }

    public String generateDescription() {

        return "Задан алфавит A = " + taskParams.get("alphabit") + " (буквы в словах" + (isMulti() ? " " : " не ") + "могут повторяться). Подсчитайте количество всeх слов длины " + sourceParams.get("nabor") + " в алфавите А" +
                (isEmptyInline() ? "." : ", для которых верно следующее: " + ((String) taskParams.get("inlineDesc")).toLowerCase() + ".");
    }

    public boolean isMulti() {
        Boolean b = (Boolean) sourceParams.get("setType-template");
        return b.booleanValue();
    }

}
