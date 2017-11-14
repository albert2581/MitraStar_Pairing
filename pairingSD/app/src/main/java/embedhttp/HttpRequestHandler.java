package embedhttp;

/**
 * Represents an Http request handler
 *
 * @author Nikki
 *
 */
public interface HttpRequestHandler {

    /**
     * Handle a request
     *
     * @param request
     *            The request to handle
     * @return The response if handled, or null to let another class handle it.
     */
    public HttpResponse handleRequest(HttpRequest request);
}