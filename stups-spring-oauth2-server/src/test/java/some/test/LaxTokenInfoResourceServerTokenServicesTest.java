/**
 * Copyright (C) 2015 Zalando SE (http://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package some.test;

import static org.assertj.core.api.StrictAssertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.web.client.RestOperations;

import org.zalando.stups.oauth2.spring.server.LaxAuthenticationExtractor;
import org.zalando.stups.oauth2.spring.server.TokenInfoResourceServerTokenServices;

import some.test.controller.SecuredResource;
import some.test.controller.TokeninfoEndpoint;

/**
 * Full Round-Trip test.<br/>
 * Tokeninfo-Endpoint faked by {@link TokeninfoEndpoint}.<br/>
 * SecuredResource found at {@link SecuredResource}.<br/>
 *
 * @author  jbellmann
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {SampleApplication.class})
@WebIntegrationTest(randomPort = false)
@DirtiesContext
@ActiveProfiles({ "custom", "laxAuthentication" })
public class LaxTokenInfoResourceServerTokenServicesTest extends AbstractTokenInfoResourceServerTokenServicesTest {

    @Autowired
    private TokenInfoResourceServerTokenServices tokenInfoResourceServerTokenServices;

    @Before
    public void setUp() {
        assertThat(tokenInfoResourceServerTokenServices.getAuthenticationExtractor()).isNotNull();
        assertThat(tokenInfoResourceServerTokenServices.getAuthenticationExtractor()).isExactlyInstanceOf(
            LaxAuthenticationExtractor.class);
    }

    @Test
    public void invokeOAuthSecuredServiceWithLaxAuthorization() {
        RestOperations restOperations = buildClient("lax");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }

    @Test
    public void invokeOAuthSecuredServiceWithLaxAuthorizationAndTheValueSetToFalse() {
        RestOperations restOperations = buildClient("lax-with-false");

        restOperations.getForEntity(getBasePath() + "/secured/hello/bello", String.class);
    }
}