/*
 * Copyright (C) 2015 Square, Inc.
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
package okhttp3

import assertk.assertThat
import assertk.assertions.containsExactly
import java.util.ArrayDeque
import java.util.Deque

class RecordingCookieJar : CookieJar {
  private val requestCookies: Deque<List<Cookie>> = ArrayDeque()
  private val responseCookies: Deque<List<Cookie>> = ArrayDeque()

  fun enqueueRequestCookies(vararg cookies: Cookie) {
    requestCookies.add(cookies.toList())
  }

  fun takeResponseCookies(): List<Cookie> = responseCookies.removeFirst()

  fun assertResponseCookies(vararg cookies: String?) {
    assertThat(takeResponseCookies().map(Cookie::toString)).containsExactly(*cookies)
  }

  override fun saveFromResponse(
    url: HttpUrl,
    cookies: List<Cookie>,
  ) {
    responseCookies.add(cookies)
  }

  override fun loadForRequest(url: HttpUrl): List<Cookie> = if (requestCookies.isEmpty()) emptyList() else requestCookies.removeFirst()
}
