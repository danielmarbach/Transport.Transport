namespace Earth
{
    using NServiceBus;
    using NServiceBus.Persistence;

    public class EndpointConfig : IConfigureThisEndpoint, AsA_Server
    {
        public void Customize(BusConfiguration configuration)
        {
            configuration.Conventions()
                .DefiningEventsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Events"))
                .DefiningCommandsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Commands"));

            configuration.UsePersistence<RavenDBPersistence>();
        }
    }
}
