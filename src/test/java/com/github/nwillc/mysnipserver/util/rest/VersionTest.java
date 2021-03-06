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

package com.github.nwillc.mysnipserver.util.rest;

import com.github.nwillc.contracts.UtilityClassContract;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest extends UtilityClassContract {
	@Override
	public Class<?> getClassToTest() {
		return Version.class;
	}

	@Test
	public void testVersionedPath() throws Exception {
		assertThat(Version.versionedPath("v2", "foo/bar")).isEqualTo("/v2/foo/bar");
	}

	@Test
	public void testVersionedDefaultPath() throws Exception {
		assertThat(Version.versionedPath("foo/bar")).isEqualTo("/v1/foo/bar");
	}
}