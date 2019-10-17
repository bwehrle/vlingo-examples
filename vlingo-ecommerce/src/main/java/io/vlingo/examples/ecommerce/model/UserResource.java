package io.vlingo.examples.ecommerce.model;

import io.vlingo.actors.AddressFactory;
import io.vlingo.actors.Stage;
import io.vlingo.actors.World;
import io.vlingo.common.Completes;
import io.vlingo.http.Response;
import io.vlingo.http.resource.ObjectResponse;
import io.vlingo.http.resource.Resource;

import static io.vlingo.http.resource.ResourceBuilder.*;
import static io.vlingo.http.resource.ResourceBuilder.get;

public class UserResource {

    public static final String ROOT_URL = "/user";
    private final CartQuery cartQuery;

    public UserResource(final CartQuery cartQuery) {
        this.cartQuery = cartQuery;
    }

    public Resource<?> routes() {

        return resource("User resource fluent api",
                        get("/user/{userId}/cartSummary")
                                .param(String.class)
                                .handle(this::queryCartSummary));
    }

    private Completes<ObjectResponse<CartUserSummaryData>> queryCartSummary(String userId) {
       return cartQuery.getCartSummaryForUser(Integer.parseInt(userId))
               .andThen( data -> ObjectResponse.of(Response.Status.Ok, data));
    }
}
