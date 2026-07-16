# Event-Driven Architecture — Sprint 2 baseline / Sprint 3 reliability / Sprint 4 visibility

Esta área reúne os contratos e convenções institucionais aprovados para a evolução incremental orientada a eventos da Sprint 2.

Kafka local, o producer do Order e o consumer inicial do Inventory estão implementados na baseline da Sprint 2. A Story #34 acrescentou recuperação local de pendências duráveis e a Story #44 acrescentou uma observação operacional local e segura do mesmo processamento. Nenhuma delas alterou o contrato `OrderConfirmed` v1, o tópico, o producer ou a integração REST.

- [Event Catalog](EVENT_CATALOG.md)
- [Convenções de eventos e tópicos](EVENT_CONVENTIONS.md)
- [Event Platform Technical Contract](../contracts/EVENT_PLATFORM_TECHNICAL_CONTRACT.md)
- [Story #34 — Architecture Gate](../contracts/STORY_034_ARCHITECTURE_GATE.md)
- [Story #44 — Architecture Gate](../contracts/STORY_044_ARCHITECTURE_GATE.md)
