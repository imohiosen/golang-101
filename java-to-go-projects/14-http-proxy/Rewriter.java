package proxy;

/**
 * Rewrites request URLs based on configured rules.
 * Supports path-prefix-based routing to different backends.
 */
public class Rewriter {

    private final Config config;

    public Rewriter(Config config) {
        this.config = config;
    }

    /**
     * Rewrite a request path to a target URL.
     * Checks rewrite rules first; falls back to default target.
     *
     * Example: /api/users → http://backend:3000/users
     */
    public String rewrite(String path) {
        for (Config.RewriteRule rule : config.getRewriteRules()) {
            if (path.startsWith(rule.getPathPrefix())) {
                String remaining = path.substring(rule.getPathPrefix().length());
                return rule.getTarget() + remaining;
            }
        }
        // Default: forward to main target
        return config.getTargetHost() + path;
    }
}
