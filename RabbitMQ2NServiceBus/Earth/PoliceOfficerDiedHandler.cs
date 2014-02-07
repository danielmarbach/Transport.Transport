namespace Earth
{
    using System;

    using Messages;
    using Messages.Commands;
    using Messages.Events;

    using NServiceBus;

    public class PoliceOfficerDiedHandler : IHandleMessages<PoliceOfficerDied>
    {
        public IBus Bus { get; set; }

        public void Handle(PoliceOfficerDied message)
        {
            Console.WriteLine("Police Officer {0} will ascend to heaven.", message.Name);

            this.Bus.Send<AscendToHeaven>(m => m.PoliceOfficer = message.Identification);
        }
    }
}