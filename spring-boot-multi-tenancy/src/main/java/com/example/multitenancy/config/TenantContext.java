package com.example.multitenancy.config;

public final class TenantContext {

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(String id) {
        tenantId.set(id);
    }

    public static String getTenantId() {
        return tenantId.get();
    }

    public static void clear() {
        tenantId.remove();
    }
}
