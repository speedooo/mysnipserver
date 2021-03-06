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

package com.github.nwillc.mysnipserver;

import com.github.nwillc.mysnipserver.controller.Authentication;
import com.github.nwillc.mysnipserver.controller.Graphql;
import com.github.nwillc.mysnipserver.entity.Category;
import com.github.nwillc.mysnipserver.entity.Snippet;
import com.github.nwillc.mysnipserver.entity.User;
import com.github.nwillc.mysnipserver.util.http.HttpException;
import com.github.nwillc.opa.Dao;
import com.google.inject.Inject;
import org.pmw.tinylog.Logger;
import spark.servlet.SparkApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class MySnipServerApplication implements SparkApplication {
    private static final String ADMIN = "nwillc@gmail.com";
    private final Dao<String, Category> categoriesDao;
    private final Dao<String, Snippet> snippetDao;
    private final Dao<String, User> userDao;
    private boolean auth;
    private String properties = "";

    @Inject
    public MySnipServerApplication(Dao<String, Category> categoriesDao,
                                   Dao<String, Snippet> snippetDao,
                                   Dao<String, User> userDao) {
        this.categoriesDao = categoriesDao;
        this.snippetDao = snippetDao;
        this.userDao = userDao;
        // bootstrap admin account
        if (!userDao.findOne(ADMIN).isPresent()) {
            Logger.warn("Bootstrapping admin account");
            userDao.save(new User(ADMIN, ADMIN));
        }
        try (
                final InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("/build.json"));
                final BufferedReader bufferedReader = new BufferedReader(isr)
        ) {
            properties = bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            Logger.warn("Could not load build info", e);
        }
        setAuth(true);
    }

    @Override
    public void init() {
        Logger.info("Starting");
        // Static files
        staticFileLocation("/public");

        // Create controllers

        if (auth) {
            new Authentication(userDao);
        }
        try {
            new Graphql(categoriesDao, snippetDao);
        } catch (Exception e) {
            Logger.error("Failed instantiating GraphGL Controller: " + e.getMessage(), e);
        }

        // Specific routes
        get("/ping", (request, response) -> "PONG");
        get("/properties", (request, response) -> properties);

        exception(HttpException.class, (e, request, response) -> {
            response.status(((HttpException) e).getCode().code);
            response.body(((HttpException) e).getCode() + ": " + e.getMessage());
            Logger.info("Returning: " + e);
        });

        Logger.info("Completed");
    }

    public void setAuth(boolean auth) {
        Logger.info("Setting authentication to: " + auth);
        this.auth = auth;
    }
}
