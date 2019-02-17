/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.elf.ui;

import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.elf.model.ElfArchParserResult;

public enum ModelProvider {
    INSTANCE;

    private List<ParserResult> persons;

    private ModelProvider() {
        persons = new ArrayList<ParserResult>();
        // Image here some fancy database access to read the persons and to
        // put them into the model
        persons.add(new ElfArchParserResult("Rainer", "Zufall"));
        persons.add(new ElfArchParserResult("Reiner", "Babbel"));
        persons.add(new ElfArchParserResult("Marie", "Dortmund"));
        persons.add(new ElfArchParserResult("Holger", "Adams"));
        persons.add(new ElfArchParserResult("Juliane", "Adams"));
    }

    public List<ParserResult> getPersons() {
        return persons;
    }

}
