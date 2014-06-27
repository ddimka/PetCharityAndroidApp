/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-06-09 16:41:44 UTC)
 * on 2014-06-27 at 07:38:28 UTC 
 * Modify at your own risk.
 */

package com.work.petcharity.kalbiyaendpoint;

/**
 * Kalbiyaendpoint request initializer for setting properties like key and userIp.
 *
 * <p>
 * The simplest usage is to use it to set the key parameter:
 * </p>
 *
 * <pre>
  public static final GoogleClientRequestInitializer KEY_INITIALIZER =
      new KalbiyaendpointRequestInitializer(KEY);
 * </pre>
 *
 * <p>
 * There is also a constructor to set both the key and userIp parameters:
 * </p>
 *
 * <pre>
  public static final GoogleClientRequestInitializer INITIALIZER =
      new KalbiyaendpointRequestInitializer(KEY, USER_IP);
 * </pre>
 *
 * <p>
 * If you want to implement custom logic, extend it like this:
 * </p>
 *
 * <pre>
  public static class MyRequestInitializer extends KalbiyaendpointRequestInitializer {

    {@literal @}Override
    public void initializeKalbiyaendpointRequest(KalbiyaendpointRequest{@literal <}?{@literal >} request)
        throws IOException {
      // custom logic
    }
  }
 * </pre>
 *
 * <p>
 * Finally, to set the key and userIp parameters and insert custom logic, extend it like this:
 * </p>
 *
 * <pre>
  public static class MyRequestInitializer2 extends KalbiyaendpointRequestInitializer {

    public MyKeyRequestInitializer() {
      super(KEY, USER_IP);
    }

    {@literal @}Override
    public void initializeKalbiyaendpointRequest(KalbiyaendpointRequest{@literal <}?{@literal >} request)
        throws IOException {
      // custom logic
    }
  }
 * </pre>
 *
 * <p>
 * Subclasses should be thread-safe.
 * </p>
 *
 * @since 1.12
 */
public class KalbiyaendpointRequestInitializer extends com.google.api.client.googleapis.services.json.CommonGoogleJsonClientRequestInitializer {

  public KalbiyaendpointRequestInitializer() {
    super();
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   */
  public KalbiyaendpointRequestInitializer(String key) {
    super(key);
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   * @param userIp user IP or {@code null} to leave it unchanged
   */
  public KalbiyaendpointRequestInitializer(String key, String userIp) {
    super(key, userIp);
  }

  @Override
  public final void initializeJsonRequest(com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest<?> request) throws java.io.IOException {
    super.initializeJsonRequest(request);
    initializeKalbiyaendpointRequest((KalbiyaendpointRequest<?>) request);
  }

  /**
   * Initializes Kalbiyaendpoint request.
   *
   * <p>
   * Default implementation does nothing. Called from
   * {@link #initializeJsonRequest(com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest)}.
   * </p>
   *
   * @throws java.io.IOException I/O exception
   */
  protected void initializeKalbiyaendpointRequest(KalbiyaendpointRequest<?> request) throws java.io.IOException {
  }
}