/*
 * This file is part of Wisetasks
 *
 * Copyright (C) 2006-2008, 2012  Michael Bogdanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.spb.ipo.engine.rmi;

import org.omg.CORBA.SystemException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.spb.ipo.engine.exception.TaskDeserializationException;
import ru.spb.ipo.engine.task.JavaTaskFactory;
import ru.spb.ipo.engine.task.ServerTask;
import ru.spb.ipo.engine.task.TaskFactory;
import ru.spb.ipo.engine.utils.FileAccessUtil;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mike
 * Date: 16.10.2008
 */
public class LocalContestProblemAccessor implements ContestProblemAccessor {

    private Contests contests;

    private Map problemsperContest = new HashMap();

    private TaskFactory factory = new JavaTaskFactory();

    public LocalContestProblemAccessor() throws TaskDeserializationException {
        contests = new Contests();
    }

    public ContestProxy[] getContestList() {
        String[] titles = contests.getTitles();
        ContestProxy[] proxies = new ContestProxy[titles.length];
        for (int i = 0; i < proxies.length; i++) {
            proxies[i] = new ContestProxy(titles[i], i);
        }
        return proxies;
    }

    public ProblemProxy[] getProblemList(long contestId) throws TaskDeserializationException, IOException {
        String folder = contests.getFile((int) contestId);
        String[] taskFiles = null;
        taskFiles = FileAccessUtil.list(folder, ".xml");
        ProblemProxy[] fileProxies = new ProblemProxy[taskFiles.length];
        for (int i = 0; i < taskFiles.length; i++) {
            fileProxies[i] = new ProblemProxy(taskFiles[i], i);
        }

        problemsperContest.put(contestId, fileProxies);

        final ProblemProxy[] proxies = new ProblemProxy[taskFiles.length];
        for (int i = 0; i < taskFiles.length; i++) {
            proxies[i] = new ProblemProxy("Название задачи не задано", i);
        }

        for (int i = 0; i < taskFiles.length; i++) {
            final int index = i;
            try {
                SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
                parser.parse(FileAccessUtil.getInputStream(getFullFileName(contestId, i)), new DefaultHandler() {
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        if ("task".equals(qName)) {
                            proxies[index].setTitle(attributes.getValue("title"));
                        }
                    }
                });
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();  //TODO:
            }
        }

        Arrays.sort(proxies, new Comparator<ProblemProxy>() {
            public int compare(ProblemProxy o1, ProblemProxy o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        return proxies;
    }

    public ServerTask getProblem(long contestId, long problemId) throws TaskDeserializationException, SystemException {
        String file = getFullFileName(contestId, problemId);
        try {
            return factory.createServerTask(file, problemId);
        } catch (IOException e) {
            throw new TaskDeserializationException("Не могу прочитать файл " + file + ":\n" + e.getMessage(), e);
        }
    }

    public String getContestName(long contestId) {
        return contests.getTitles()[(int) contestId];
    }

    private String getFileName(long contestId, long problemId) {
        String fileName = ((ProblemProxy[]) problemsperContest.get(contestId))[(int) problemId].getTitle();
        return fileName;
    }

    public String getFullFileName(long contestId, long problemId) {
        String fileName = ((ProblemProxy[]) problemsperContest.get(contestId))[(int) problemId].getTitle();
        return contests.getFile((int) contestId) + "/" + fileName;
    }

    public TaskFactory getFactory() {
        return factory;
    }
}
