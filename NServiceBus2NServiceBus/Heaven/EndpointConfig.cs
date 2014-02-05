namespace Heaven
{
    using NServiceBus;

	public class EndpointConfig : IConfigureThisEndpoint, AsA_Server, INeedInitialization
    {
        public void Init()
        {
            Configure.With()
                .DefiningEventsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Events"))
                .DefiningCommandsAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Commands"))
                .DefiningMessagesAs(t => t.Namespace != null && t.Namespace.StartsWith("Messages.Messages"))
                .RavenSubscriptionStorage();
        }
    }
}
