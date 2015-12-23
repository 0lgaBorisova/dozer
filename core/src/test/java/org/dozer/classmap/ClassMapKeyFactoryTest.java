/**
 * Copyright 2005-2013 Dozer Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dozer.classmap;

import org.dozer.AbstractDozerTest;
import org.dozer.cache.CacheKeyFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dmitry.buzdin
 */
public class ClassMapKeyFactoryTest extends AbstractDozerTest {

  private CacheKeyFactory factory;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testCreateKey() {
      CacheKeyFactory.CacheKey key1 = CacheKeyFactory.create(String.class, Long.class);
      CacheKeyFactory.CacheKey key2 = CacheKeyFactory.create(String.class, Long.class);
    assertEquals(key1, key2);
  }
  
  @Test
  public void testCreateKey_Order() {
      CacheKeyFactory.CacheKey key1 = CacheKeyFactory.create(String.class, Long.class);
      CacheKeyFactory.CacheKey key2 = CacheKeyFactory.create(Long.class, String.class);
    assertNotSame(key1, key2);
    assertFalse(key1.equals(key2));
  }

  @Test
  public void testCreateKey_MapId() {
      CacheKeyFactory.CacheKey key1 = CacheKeyFactory.create(String.class, Long.class, "id");
      CacheKeyFactory.CacheKey key2 = CacheKeyFactory.create(String.class, Long.class);
    assertNotSame(key1, key2);
    assertFalse(key1.equals(key2));
  }
  
  @Test
  public void testCreateKey_MapIdNull() {
      CacheKeyFactory.CacheKey key = CacheKeyFactory.create(String.class, Long.class, null);
    assertNotNull(key);
  }

}
