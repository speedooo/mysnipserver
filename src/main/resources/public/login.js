/*
 * Copyright (c) 2016,  nwillc@gmail.com
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
 */

var APP = APP || {};

APP.Login = function () {
    // Instance variables
    this.username = $('#username');
    this.password = $('#password');
    this.personaButton = $('#personaButton');


    // Event handlers
    this.validUsername = () => {
        var notValid = !$(this.username).val().match(/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i);

        $(this.password).prop('disabled', notValid);
        $(this.personaButton).prop('disabled', notValid);
    }

    this.personaLogin = () => {
        APP.persona.login($(this.username).val());
    }

    this.login = (event) => {
        if (event.keyCode === 13) {
            $.get("v1/auth/" + $(this.username).val() + "/" + $(this.password).val(), function () {
                window.location.replace("/");
            });
        }
    }

    // Bindings
    $(this.username).keyup(this.validUsername);
    $(this.password).keyup(this.login);
    $(this.personaButton).click(this.personaLogin);

    // Go!
    this.validUsername();
    $(this.username).focus();
};

$(document).ready(function () {
    new APP.Login();
});

