namespace Earth
{
    using System;

    using Messages.Commands;

    using NServiceBus;

    public class HuntDeadosOnEarthHandler : IHandleMessages<HuntDeadosOnEarth>
    {
        public IBus Bus { get; set; }

        public void Handle(HuntDeadosOnEarth message)
        {
            Console.WriteLine("Police Officer {0} is back hunting deados!", message.Name);
        }
    }
}