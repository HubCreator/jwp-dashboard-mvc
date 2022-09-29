package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.controller.tobe.HandlerAdapterRegistry;
import nextstep.mvc.controller.tobe.HandlerMappingRegistry;
import nextstep.mvc.controller.tobe.ViewResolverRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final ViewResolverRegistry viewResolverRegistry;

    public DispatcherServlet(final HandlerMappingRegistry handlerMappingRegistry,
                             final HandlerAdapterRegistry handlerAdapterRegistry,
                             final ViewResolverRegistry viewResolverRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
        this.viewResolverRegistry = viewResolverRegistry;
    }

    @Override
    public void init() {
        log.info("DispatcherServlet Start");
        handlerMappingRegistry.getHandlerMappings()
                .forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolverRegistry.addViewResolver(viewResolver);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();

        if (modelAndView.hasViewImpl()) {
            final View view = modelAndView.getView();
            view.render(model, request, response);
            return;
        }

        final View jspView = resolveView(modelAndView);
        jspView.render(model, request, response);
    }

    private View resolveView(final ModelAndView modelAndView) {
        final ViewResolver jspViewResolver = viewResolverRegistry.getJspViewResolver();
        return jspViewResolver.resolveViewName(modelAndView.getViewName());
    }
}
