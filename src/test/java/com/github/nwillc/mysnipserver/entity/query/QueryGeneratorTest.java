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

package com.github.nwillc.mysnipserver.entity.query;

import com.github.nwillc.mysnipserver.entity.Entity;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class QueryGeneratorTest {
    private QueryGenerator<Bean> queryGenerator;

    @Before
    public void setUp() throws Exception {
        queryGenerator = new QueryGenerator<>();
    }

    @Test
    public void testEqPredicate() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "42");
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("key", "42");
        assertThat(predicate.test(bean)).isTrue();
        bean = new Bean("key", "NaN");
        assertThat(predicate.test(bean)).isFalse();
    }

    @Test
    public void testNotPredicate() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "key", "10").not();
        Predicate<Bean> predicate = generator.toPredicate();

        Bean bean = new Bean("10", "42");
        assertThat(predicate.test(bean)).isFalse();
        bean = new Bean("key", "NaN");
        assertThat(predicate.test(bean)).isTrue();
    }

    @Test
    public void testOrPredicateTrue() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "42")
                .contains(Bean.class, "value", "43")
                .or();
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("key", "42");
        assertThat(predicate.test(bean)).isTrue();

        bean = new Bean("key", "22");
        assertThat(predicate.test(bean)).isFalse();
    }

    @Test
    public void testAndPredicateTrue() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "42")
                .contains(Bean.class, "value", "42")
                .and();
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("key", "42");
        assertThat(predicate.test(bean)).isTrue();
    }

    @Test
    public void testComplexPredicateTrue() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "foo")
                .contains(Bean.class, "second", "foo")
                .or()
                .contains(Bean.class, "key", "1")
                .and();
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("1", "test");
        bean.setSecond("foo");
        assertThat(predicate.test(bean)).isTrue();
    }

    @Test
    public void testComplexPredicateFalse() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "foo")
                .contains(Bean.class, "second", "foo")
                .or()
                .contains(Bean.class, "key", "2")
                .and();
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("1", "test");
        bean.setSecond("foo");
        assertThat(predicate.test(bean)).isFalse();
    }

    @Test
    public void testAndPredicateFalse() throws Exception {
        QueryGenerator<Bean> generator = queryGenerator
                .contains(Bean.class, "value", "42")
                .contains(Bean.class, "value", "44")
                .and();
        Predicate<Bean> predicate = generator.toPredicate();
        Bean bean = new Bean("key", "42");
        assertThat(predicate.test(bean)).isFalse();
    }

    @Test
    public void testEq() throws Exception {
        QueryGenerator generator = queryGenerator
                .contains(Bean.class, "key", "42");
        assertThat(generator.toString()).isEqualTo("contains(\"key\",\"42\")");
    }

    @Test
    public void testNot() throws Exception {
        QueryGenerator generator = queryGenerator
                .contains(Bean.class, "key","1")
                .not();
        assertThat(generator.toString()).isEqualTo("not(contains(\"key\",\"1\"))");
    }

    @Test
    public void testAnd() throws Exception {
        QueryGenerator generator = queryGenerator
                .contains(Bean.class, "key","1")
                .contains(Bean.class, "key","2")
                .and();
        assertThat(generator.toString()).isEqualTo("and(contains(\"key\",\"1\"),contains(\"key\",\"2\"))");

    }

    @Test
    public void testOr() throws Exception {
        QueryGenerator generator = queryGenerator
                .contains(Bean.class, "key","1")
                .contains(Bean.class, "key","2")
                .or();
        assertThat(generator.toString()).isEqualTo("or(contains(\"key\",\"1\"),contains(\"key\",\"2\"))");

    }

    public class Bean extends Entity {
        private final String value;
        private String second;

        public Bean(String key, String value) {
            super(key);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "key='" + getKey() + '\'' +
                    " value='" + value + '\'' +
                    ", second='" + second + '\'' +
                    '}';
        }
    }
}