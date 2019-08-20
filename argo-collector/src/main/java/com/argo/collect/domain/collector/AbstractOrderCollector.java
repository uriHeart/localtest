package com.argo.collect.domain.collector;

import com.argo.collect.domain.auth.AuthorityManager;
import com.argo.collect.domain.enums.SalesChannel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractOrderCollector implements OrderCollector {
    @Autowired
    private List<AuthorityManager> authorityManagers;

    protected AuthorityManager getAuth(SalesChannel channel) {
        return authorityManagers.stream().filter(a -> a.isTargetChannel(channel)).findFirst().orElse(null);
    }
}
