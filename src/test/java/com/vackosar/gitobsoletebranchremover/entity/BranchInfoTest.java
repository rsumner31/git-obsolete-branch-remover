package com.vackosar.gitobsoletebranchremover.entity;

import org.junit.Assert;
import org.junit.Test;

public class BranchInfoTest {
    @Test
    public void removePrefix() {
        final BranchInfo info = new BranchInfo(null, "refs/remotes/origin/feature/alpha", "a@b.cz", true);
        Assert.assertEquals("feature/alpha", info.branchName);
        Assert.assertEquals("origin", info.remoteName.get());
        Assert.assertEquals(BranchType.remote, info.branchType);
    }
}
