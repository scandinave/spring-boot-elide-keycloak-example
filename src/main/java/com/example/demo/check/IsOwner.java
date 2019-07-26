package com.example.demo.check;

import com.example.demo.models.User;
import com.yahoo.elide.security.ChangeSpec;
import com.yahoo.elide.security.RequestScope;
import com.yahoo.elide.security.checks.CommitCheck;
import com.yahoo.elide.security.checks.OperationCheck;
import java.security.Principal;
import java.util.Optional;

public class IsOwner {
    public static class Inline extends OperationCheck<User> {

        @Override
        public boolean ok(User user, RequestScope requestScope, Optional<ChangeSpec> changeSpec) {
            return user.getName().equals(((Principal) requestScope.getUser().getOpaqueUser()).getName());
        }
    }

    public static class AtCommit extends CommitCheck<User> {

        @Override
        public boolean ok(User user, RequestScope requestScope, Optional<ChangeSpec> changeSpec) {
            return user.getName().equals(((Principal) requestScope.getUser().getOpaqueUser()).getName());
        }
    }
}