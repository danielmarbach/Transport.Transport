namespace Earth
{
    using NServiceBus;

    // We need to initialize this.
    public class EndpointConfig : IConfigureThisEndpoint, AsA_Server
    {
        public void Customize(BusConfiguration configuration)
        {
        }
    }
}
