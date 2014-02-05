namespace Heaven
{
    using System;

    using Messages;
    using Messages.Events;

    using NServiceBus;

    public class PoliceOfficerDiedHandler : IHandleMessages<PoliceOfficerDied>
    {
        public IBus Bus { get; set; }

        public void Handle(PoliceOfficerDied message)
        {
            Console.WriteLine("Department: New police officer {0} arrived in heaven.", message.Name);

            var officer = new PoliceOfficer(message.Identification, message.Name);

            Departments.RegisterOfficer(officer);
        }
    }
}