package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class ShippedEventAdapter implements EntryAdapter<OrderEvents.OrderShipped,Entry.TextEntry> {

    @Override
    public OrderEvents.OrderShipped fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, OrderEvents.OrderShipped.class);
    }

    @Override
    public Entry.TextEntry toEntry(final OrderEvents.OrderShipped source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(OrderEvents.OrderShipped.class, 1, serialization, Metadata.nullMetadata());
    }
}
