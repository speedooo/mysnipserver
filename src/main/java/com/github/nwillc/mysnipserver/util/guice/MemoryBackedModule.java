/*
 * Copyright (c) 2016, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 */

package com.github.nwillc.mysnipserver.util.guice;

import com.github.nwillc.mysnipserver.MySnipServerApplication;
import com.github.nwillc.mysnipserver.dao.memory.CategoryDao;
import com.github.nwillc.mysnipserver.dao.memory.SnippetDao;
import com.github.nwillc.opa.memory.MemoryBackedDao;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.pmw.tinylog.Logger;


public class MemoryBackedModule extends AbstractModule {

    @Override
    protected void configure() {
        Logger.info("Configuring Memory Backed module.");
        final CategoryDao categoryDao = new CategoryDao();
        bind(new TypeLiteral<MySnipServerApplication>() {})
                .toInstance(new MySnipServerApplication(categoryDao, new SnippetDao(categoryDao), new MemoryBackedDao<>()));
    }
}
