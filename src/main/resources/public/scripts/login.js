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

define(["cookies", "jquery"], function (cookies, $) {
    return {
        Login: function () {
            "use strict";
            var _this = this;

            // Instance variables
            this.username = $("#username");
            this.password = $("#password");

            // Event handlers
            this.validUsername = function () {
                var notValid = !$(_this.username).val().match(/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i);

                $(_this.password).prop("disabled", notValid);
                $(_this.authButton).prop("disabled", notValid);
            };

            this.login = function (event) {
                if (event.keyCode === 13) {
                    $.get("v1/auth/" + $(_this.username).val() + "/" + $(_this.password).val(), function () {
                        window.location.replace("/");
                    });
                }
            };

            this.onSignIn = function (googleUser) {
                $.get("v1/auth/" + googleUser.getAuthResponse().id_token, function () {
                    cookies.set("token", googleUser.getAuthResponse().id_token);
                    window.location.replace("/");
                });
            };

            // Bindings
            $(this.username).keyup(this.validUsername);
            $(this.password).keyup(this.login);

            // Go!
            this.validUsername();
            $(this.username).focus();
        }
    }
});
