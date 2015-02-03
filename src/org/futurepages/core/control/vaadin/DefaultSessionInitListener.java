package org.futurepages.core.control.vaadin;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import org.jsoup.nodes.Element;

@SuppressWarnings("serial")
public class DefaultSessionInitListener implements SessionInitListener {

    @Override
    public final void sessionInit(final SessionInitEvent event)
            throws ServiceException {

        event.getSession().addBootstrapListener(new BootstrapListener() {

            @Override
            public void modifyBootstrapPage(final BootstrapPageResponse response) {
                final Element head = response.getDocument().head();

                head.appendElement("meta")
                        .attr("name", "viewport")
                        .attr("content", "width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no");

                head.appendElement("meta")
                        .attr("name", "apple-mobile-web-apps-capable")
                        .attr("content", "yes");
                head.appendElement("meta")
                        .attr("name", "apple-mobile-web-apps-status-bar-style")
                        .attr("content", "black-translucent");

                String contextPath = response.getRequest().getContextPath();

                head.appendElement("link")
                        .attr("rel", "apple-touch-icon")
                        .attr("href", contextPath + "/VAADIN/mobile/img/apple-touch-icon.png");

            }

            @Override
            public void modifyBootstrapFragment(
                    final BootstrapFragmentResponse response) {
            }
        });
    }

}
