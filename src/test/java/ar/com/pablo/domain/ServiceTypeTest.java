package ar.com.pablo.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import ar.com.pablo.web.rest.TestUtil;

public class ServiceTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceType.class);
        ServiceType serviceType1 = new ServiceType();
        serviceType1.setId("id1");
        ServiceType serviceType2 = new ServiceType();
        serviceType2.setId(serviceType1.getId());
        assertThat(serviceType1).isEqualTo(serviceType2);
        serviceType2.setId("id2");
        assertThat(serviceType1).isNotEqualTo(serviceType2);
        serviceType1.setId(null);
        assertThat(serviceType1).isNotEqualTo(serviceType2);
    }
}
