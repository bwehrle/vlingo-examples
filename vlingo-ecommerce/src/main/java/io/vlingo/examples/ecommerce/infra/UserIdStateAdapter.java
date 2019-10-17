package io.vlingo.examples.ecommerce.infra;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.CartUserSummaryData;
import io.vlingo.examples.ecommerce.model.UserId;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State;
import io.vlingo.symbio.StateAdapter;


public class UserIdStateAdapter implements StateAdapter<UserId, State.TextState> {

    @Override
    public int typeVersion() {
        return 0;
    }

    @Override
    public UserId fromRawState(State.TextState textState) {
        return JsonSerialization.deserialized(textState.data, textState.typed());
    }

    @Override
    public <ST> ST fromRawState(State.TextState textState, Class<ST> aClass) {
        return JsonSerialization.deserialized(textState.data, aClass);
    }

    @Override
    public State.TextState toRawState(String id, UserId state, int stateVersion, Metadata metadata) {
        final String serialization = JsonSerialization.serialized(state);
        return new State.TextState(id, CartUserSummaryData.class, typeVersion(), serialization, stateVersion, metadata);
    }
}
