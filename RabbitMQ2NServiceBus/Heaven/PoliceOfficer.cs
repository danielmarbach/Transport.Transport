namespace Heaven
{
    using System;

    public class PoliceOfficer
    {
        private readonly Guid identification;

        private readonly string name;

        public PoliceOfficer(Guid identification, string name)
        {
            this.name = name;
            this.identification = identification;
        }

        public Guid Identification
        {
            get
            {
                return this.identification;
            }
        }

        public bool IsRecruited { get; private set; }

        public string Name
        {
            get
            {
                return this.name;
            }
        }

        public void Recruit()
        {
            this.IsRecruited = true;
        }
    }
}