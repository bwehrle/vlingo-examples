package io.vlingo.examples.ecommerce.infra;

import io.vlingo.actors.*;
import io.vlingo.common.Completes;
import io.vlingo.examples.ecommerce.model.Cart;
import io.vlingo.examples.ecommerce.model.UserId;
import io.vlingo.examples.ecommerce.model.ProductId;
import io.vlingo.examples.ecommerce.model.CartEntity;
import io.vlingo.http.Response;
import io.vlingo.http.resource.Resource;

import static io.vlingo.common.serialization.JsonSerialization.serialized;
import static io.vlingo.http.Response.Status.*;
import static io.vlingo.http.ResponseHeader.*;
import static io.vlingo.http.resource.ResourceBuilder.*;

public class CartResource {
    public static final String ROOT_URL = "/cart";
    private final AddressFactory addressFactory;
    private final Stage stage;

    public CartResource(final World world) {
        this.addressFactory = world.addressFactory();
        this.stage = world.stage();
    }

    public Completes<Response> create(final UserId userId) {
        // Check if already exists and then return 404?
        final Address cartAddress = addressFactory.uniquePrefixedWith("sc-");

        stage.actorFor(Definition.has(CartEntity.class,
                Definition.parameters(cartAddress.idString(), userId)),
                Cart.class,
                cartAddress);

        return Completes.withSuccess(
                Response.of(Created,
                        headers(of(Location, location(cartAddress.idString()))),
                        ""));
    }

    public Completes<Response> queryCart(String cartId) {
        return stage.actorOf(addressFactory.from(cartId), CartEntity.class)
                .andThenTo(cart -> Completes.withSuccess(Response.of(Ok, "")))
                .otherwise(noUser -> Response.of(NotFound, location(cartId)));
    }

    private String doChangeItem(CartEntity entity, String idOfProduct, CartItemChange change) {
        ProductId productId = ProductId.fromId(idOfProduct);
        if (change.isAdd())
            entity.addItem(productId);
        else
            entity.removeItem(productId);

        return serialized(entity.queryCart());
    }

    public Completes<Response> changeCart(String cartId, String productId, CartItemChange change) {
        return stage.actorOf(addressFactory.from(cartId), CartEntity.class)
                .andThenTo(cart -> Completes.withSuccess(Response.of(Ok, doChangeItem(cart, productId, change))))
                .otherwise(noUser -> Response.of(NotFound, location(cartId)));
    }


    private String location(final String shoppingCartId) {
        return ROOT_URL + "/" + shoppingCartId;
    }


    public static class CartItemChange {

        public CartItemChange(String operation) {
            this.operation = operation;
        }

        public final String operation;

        public boolean isAdd() {
            return operation.equals("add");
        }
    }

    public Resource routes() {

        return resource("Cart resource fluent api",
                post("/cart")
                        .body(UserId.class)
                        .handle(this::create),
                patch("/cart/{cartId}/{productId}")
                        .param(String.class)
                        .param(String.class)
                        .body(CartItemChange.class)
                        .handle(this::changeCart),
                get("/cart/{cartId}")
                        .param(String.class)
                        .handle(this::queryCart));
    }
}

