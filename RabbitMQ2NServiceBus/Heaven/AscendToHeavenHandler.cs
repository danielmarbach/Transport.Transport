namespace Heaven
{
    using Messages;
    using Messages.Commands;
    using Messages.Events;
    using Messages.Messages;

    using NServiceBus;

    public class AscendToHeavenHandler : IHandleMessages<AscendToHeaven>
    {
        public IBus Bus { get; set; }

        public void Handle(AscendToHeaven message)
        {
            PoliceOfficer officer = Departments.SelectForRecruitment(message.PoliceOfficer);
            if (officer == null)
            {
                return;
            }

            Departments.RecruitByMildredProctor(officer);

            if (officer.IsRecruited)
            {
                this.Bus.SendLocal<DeadPoliceOfficerRecruited>(
                    m =>
                    {
                        m.Identification = officer.Identification;
                        m.Name = officer.Name;
                    });
            }
        }
    }
}