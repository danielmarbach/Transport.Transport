namespace Earth
{
    using NServiceBus;

    public class EndpointConfig : IConfigureThisEndpoint, AsA_Server, INeedInitialization, UsingTransport<RabbitMQ>
    {
        public void Init()
        {
            Configure.With()
                .DefiningEventsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Events"))
                .DefiningCommandsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Commands"))
                .UseInMemoryTimeoutPersister();
        }
    }
}
