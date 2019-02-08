package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class OrderCreatedEventAdapter implements EntryAdapter<OrderEvents.Created,Entry.TextEntry> {

    @Override
    public OrderEvents.Created fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, OrderEvents.Created.class);
    }

    @Override
    public Entry.TextEntry toEntry(final OrderEvents.Created source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(OrderEvents.Created.class, 1, serialization, Metadata.nullMetadata());
    }
}
