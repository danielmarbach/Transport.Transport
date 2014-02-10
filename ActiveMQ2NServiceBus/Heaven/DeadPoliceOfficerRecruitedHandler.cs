namespace Heaven
{
    using Messages.Commands;
    using Messages.Events;
    using Messages.Messages;

    using NServiceBus;

    public class DeadPoliceOfficerRecruitedHandler : IHandleMessages<DeadPoliceOfficerRecruited>
    {
        public IBus Bus { get; set; }

        public void Handle(DeadPoliceOfficerRecruited message)
        {
            PoliceOfficer officer = Departments.HuntDeados(message.Identification);

            this.Bus.Send<HuntDeadosOnEarth>(
                m =>
                    {
                        m.PoliceOfficer = officer.Identification;
                        m.Name = officer.Name;
                    });
        }
    }
}