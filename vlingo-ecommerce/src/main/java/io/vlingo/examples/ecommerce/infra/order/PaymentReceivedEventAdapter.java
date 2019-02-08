package io.vlingo.examples.ecommerce.infra.order;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.examples.ecommerce.model.OrderEvents;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public class PaymentReceivedEventAdapter implements EntryAdapter<OrderEvents.PaymentReceived,Entry.TextEntry> {

    @Override
    public OrderEvents.PaymentReceived fromEntry(final Entry.TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData, OrderEvents.PaymentReceived.class);
    }

    @Override
    public Entry.TextEntry toEntry(final OrderEvents.PaymentReceived source) {
        final String serialization = JsonSerialization.serialized(source);
        return new Entry.TextEntry(OrderEvents.PaymentReceived.class, 1, serialization, Metadata.nullMetadata());
    }
}
