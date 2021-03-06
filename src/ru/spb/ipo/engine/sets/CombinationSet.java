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

package ru.spb.ipo.engine.sets;

import ru.spb.ipo.engine.elements.ContainerElement;
import ru.spb.ipo.engine.elements.Element;
import ru.spb.ipo.engine.exception.SystemException;
import ru.spb.ipo.engine.exception.TaskDeserializationException;
import ru.spb.ipo.engine.task.Node;
import ru.spb.ipo.engine.utils.MathOperations;

import java.util.List;

public class CombinationSet extends Set {

    private Set set;

    private long size;

    //размер сочетания
    private int clength;

    //размер множества
    private int slength;

    private List listOfElements;

    public CombinationSet() {
    }

    public CombinationSet(Set source, int clength) {
        this.clength = clength;
        slength = (int) source.getSize();

        set = source;

        size = MathOperations.combination(source.getSize(), clength).longValue();

        checkDublicates(source);
    }

    private void checkDublicates(Set source) {
//        listOfElements = new ArrayList();
//        while (source.hasNext()) {
//            listOfElements.add(source.next().clone());
//        }
//        Collections.sort(listOfElements);
    }


//    public boolean hasNext() {
////        if (index == 0 || !listOfElements.get(listOfElements.size()-1).equals(listOfElements.get(combination[combination.length - 1]-1))) {
////            return true;
////        }
////        int i = clength - 1;
////        int temp = combination[i];
////        while(i >= 0) {
////            if (temp < slength - (clength - 1 - i)) {
////                temp++;
////                if (!listOfElements.get(temp - 1).equals(listOfElements.get(temp-2))) {
////                    return true;
////                }
////            } else {
////                i--;
////                if (i >= 0) {
////                    temp = combination[i];
////                }
////            }
////        }
//        if (index < size) return true;
//    	/*for (int i = 0; i < combination.length; i++) {
//    		if (combination[i] != slength + i - combination.length + 1) return true;
//    	}*/
//        return false;
//    }
//
//    public Element getCurrent() {
//        comb2Element();
//        return elm;
//    }

//    public Element next() {
//        if (!hasNext()) return null;
//
//        if (index == 0) {
//            for (int i = 0; i < clength; i++) {
//                combination[i] = i + 1;
//            }
//        } else {
//            boolean stop = false;
//            int i = clength - 1;
//            while(!stop && i >= 0) {
//                if (combination[i] < slength - (clength - 1 - i)) {
//                    combination[i]++;
//                    //if (!listOfElements.get(combination[i] - 1).equals(listOfElements.get(combination[i]-2))) {
//                        stop = true;
//                    //}
//                } else i--;
//            }
//            for (int j = i + 1; j < clength; j++)
//                combination[j] = combination[i] + j - i;
//        }
//        comb2Element();
//        index++;
//        return elm;
//    }

//    private Element comb2Element(){
//        for (int i = 0; i < clength; i++) {
//            set[i].getElement(combination[i]);
//            //elm.setElementAt(i, (Element)listOfElements.get(combination[i] - 1));
//        }
//    }
//
//    public void reset() {
//        index = 0;
//        for (int i = 0; i < clength; i++)
//            set[i].getElement(i);
//    }


    protected void initSet(Node node) throws TaskDeserializationException, SystemException {
        Node sset = node.getChild("set");
        Set source = Set.generateSet(sset);
        slength = (int) source.getSize();
        String length = node.getAttr("length");
        clength = new Integer(length);

        set = source;

        size = MathOperations.combination(source.getSize(), clength).longValue();

        checkDublicates(source);
    }

    public long getSize() {
        return size;
    }

    public Element getElement(long index) {
        throw new UnsupportedOperationException("Operation getElement(long index) isn't supported in CombinationSet class");

    }

    public Object clone() {
        throw new UnsupportedOperationException("Operation clone isn't supported in CombinationSet class");
    }

    public int getLength() {
        return clength;
    }

    public SetIterator iterator() {

        return new SetIterator() {
            private long index = 0;

            private int[] combination = new int[clength];

            public Element getCurrent() {
                return comb2Element();
            }

            public Element next() {
                if (!hasNext()) return null;

                if (index == 0) {
                    for (int i = 0; i < clength; i++) {
                        combination[i] = i + 1;
                    }
                } else {
                    boolean stop = false;
                    int i = clength - 1;
                    while (!stop && i >= 0) {
                        if (combination[i] < slength - (clength - 1 - i)) {
                            combination[i]++;
                            //if (!listOfElements.get(combination[i] - 1).equals(listOfElements.get(combination[i]-2))) {
                            stop = true;
                            //}
                        } else i--;
                    }
                    for (int j = i + 1; j < clength; j++)
                        combination[j] = combination[i] + j - i;
                }
                Element res = comb2Element();
                index++;
                return res;
            }

            private Element comb2Element() {
                Element[] elms = new Element[clength];
                for (int i = 0; i < clength; i++) {
                    elms[i] = set.getElement(combination[i]);
                }
                return new ContainerElement(elms);
            }

            public void reset() {
                index = 0;
            }

            public boolean hasNext() {
                return index < size;
            }

        };
    }
}
